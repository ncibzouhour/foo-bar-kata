package com.examples.foobarkata.infra;


import com.examples.foobarkata.domain.ports.api.IntProcessingRequester;
import com.examples.foobarkata.domain.ports.spi.IntBatchProcessingGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.function.Function;

@Component
@Slf4j
@EnableScheduling
public class FooBarIntFileProcessorBatch implements IntBatchProcessingGateway {


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

    @Autowired
    private JobLauncher jobLauncher;


    public void processIntBatchToString(Function<Integer, String> transformationFunction) {
        Job job = job(transformationFunction);
        try {
            jobLauncher.run(job, new JobParameters());
        }  catch (Exception e) {
            log.error("Error occurred : {}", e.getMessage());
        }
    }

    public FlatFileItemReader<Integer> fileReader() {
        FlatFileItemReader<Integer> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(inputFilePath));
        reader.setLineMapper((line, lineNumber) -> {
            try {
                return Integer.parseInt(line.strip());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid line at " + lineNumber + ": " + line, e);
            }
        });
        return reader;
    }

    public ItemProcessor<Integer, String> processor(Function<Integer, String> transformationSupplier) {
        return item -> {
            // concat int and the string processed
            StringBuilder sb = new StringBuilder();
            sb.append(item)
                    .append("\t\"")
                    .append(transformationSupplier.apply(item))
                    .append("\"");
            return sb.toString();
        };
    }

    public FlatFileItemWriter<String> fileWriter() {
        FlatFileItemWriter<String> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(outputFilePath));
        writer.setLineAggregator(new PassThroughLineAggregator<>());
        return writer;
    }


    public Job job(Function<Integer, String> transformationSupplier) {
        return new JobBuilder("FileProcessingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(step(transformationSupplier))
                .end()
                .build();
    }

    public Step step(Function<Integer, String> transformationSupplier) {
        return new StepBuilder("file-processing-step", jobRepository)
                .<Integer, String> chunk(chunkSize, transactionManager)
                .reader(fileReader())
                .processor(processor(transformationSupplier))
                .writer(fileWriter())
                .faultTolerant()
                .skip(IllegalArgumentException.class) // Skip lines with invalid format
                .build();
    }


}
