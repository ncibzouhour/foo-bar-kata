package com.examples.foobarkata.domain.ports.spi;

import java.util.Map;

public interface ResultProducer {

    void produceResult(Map<Integer, String> resultMap);
}
