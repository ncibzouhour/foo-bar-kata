package com.examples.foobarkata.domain.service;

import com.examples.foobarkata.domain.ports.api.IntBatchProcessingRequester;
import com.examples.foobarkata.domain.ports.spi.IntBatchProcessingGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntBatchProcessingService implements IntBatchProcessingRequester {

    @Autowired
    private IntBatchProcessingGateway intBatchProcessingGateway;

    @Autowired
    private IntProcessingService intProcessingService;

    @Override
    public void processIntBatchToString() throws IllegalArgumentException {
        intBatchProcessingGateway.processIntBatchToString(intProcessingService::processIntToString);
    }

}
