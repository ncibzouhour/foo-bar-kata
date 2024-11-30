package com.examples.foobarkata.api.batch;


import com.examples.foobarkata.domain.ports.api.IntProcessingRequester;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@Slf4j
public class FooBarIntFileProcessorBatchConfig {

    @Value("${foobar-int-file-processor-batch.input.file.path}")
    private String inputFilePath;

    @Value("${foobar-int-file-processor-batch.output.file.path}")
    private String outputFilePath;

    @Value("${foobar-int-file-processor-batch.chunk-size}")
    private int chunkSize;

    @Autowired
    private IntProcessingRequester intProcessingRequester;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public FlatFileItemReader<Integer> fileReader() {
        FlatFileItemReader<Integer> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(inputFilePath));
        reader.setLineMapper((line, lineNumber) -> {
            return Integer.parseInt(line.strip()); // trim() pour éviter les espaces blancs
        });
        return reader;
    }

    @Bean
    public ItemProcessor<Integer, String> processor() {
        return item -> {
            // concat int and the string processed
            StringBuilder sb = new StringBuilder();
            sb.append(item).append("\t\"").append(intProcessingRequester.processIntToString(item)).append("\"");
            return sb.toString();
        };
    }

    @Bean
    public FlatFileItemWriter<String> fileWriter() {
        FlatFileItemWriter<String> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(outputFilePath));
        writer.setLineAggregator(new PassThroughLineAggregator<>());
        return writer;
    }


    @Bean
    public Job job(Step step) {
        return new JobBuilder("FileProcessingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }


    @Bean
    public Step step(FlatFileItemReader<Integer> fileReader,
                     ItemProcessor<Integer, String> processor,
                     FlatFileItemWriter<String> fileWriter) {
        return new StepBuilder("file-processing-step", jobRepository)
                .<Integer, String> chunk(chunkSize, transactionManager)
                .reader(fileReader)
                .processor(processor)
                .writer(fileWriter)
                .build();
    }
}
