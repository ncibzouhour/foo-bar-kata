package com.examples.foobarkata.api.batch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FooBarIntFileProcessorBatchIT {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private FooBarIntFileProcessorBatch batchProcessor; // Injecter le batch pour vérifier la méthode

    @Value("${foobar-int-file-processor-batch.input.file.path}")
    private String inputFilePath;

    @Value("${foobar-int-file-processor-batch.output.file.path}")
    private String outputFilePath;

    Path inputPath, outputPath;
    @BeforeEach
    public void setUp() throws Exception {
        // Prepare test data (replace with your data generation logic)
        inputPath = Paths.get(inputFilePath);
        outputPath = Paths.get(outputFilePath);

    }

    @AfterEach
    public void tearDown() throws Exception {
        // Clean up test data
        Files.delete(outputPath);
    }

    @Test
    public void testJobExecution() throws Exception {
        Files.write(inputPath, Arrays.asList("1", "3", "5", "7", "9"));
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

        Path outputPath = Paths.get(outputFilePath);
        List<String> outputLines = Files.readAllLines(outputPath);
        assertEquals(Arrays.asList("1\t\"1\"", "3\t\"FOOFOO\"", "5\t\"BARBAR\"", "7\t\"QUIX\"", "9\t\"FOO\""), outputLines);


    }
}