package com.examples.foobarkata.api.batch;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
public class FooBarIntFileProcessorBatch {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;


    @Scheduled(cron = "${foobar-int-file-processor-batch.scheduler}")
    //@EventListener(ApplicationReadyEvent.class)
    public void processIntFile() {
        log.info( "Scheduled processIntFile job is started");
        try {
            jobLauncher.run(job, new JobParameters());
        }  catch (Exception e) {
            log.error("Error occurred : {}", e.getMessage());
        }
    }
}
