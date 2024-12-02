package com.examples.foobarkata.domain.ports.spi;

import java.util.function.Function;

public interface IntBatchProcessingGateway {

    void processIntBatchToString(Function<Integer, String> transformationFunction) throws IllegalArgumentException;

}
