package com.examples.foobarkata.api.batch;


import com.examples.foobarkata.domain.ports.api.IntBatchProcessingRequester;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
public class FooBarIntFileProcessorBatchApp {

    @Autowired
    private IntBatchProcessingRequester intBatchProcessingRequester;


    @Scheduled(cron = "${foobar-int-file-processor-batch.scheduler}")
    //@EventListener(ApplicationReadyEvent.class)
    public void processIntBatch() {
        log.info( "Scheduled processIntBatch job is started");
        try {
            intBatchProcessingRequester.processIntBatchToString();
        }  catch (Exception e) {
            log.error("Error occurred : {}", e.getMessage());
        }
    }
}
