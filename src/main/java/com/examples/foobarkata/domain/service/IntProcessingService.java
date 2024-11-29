package com.examples.foobarkata.domain.service;

import com.examples.foobarkata.domain.ports.StringProcessingRequester;

public class StringProcessingService implements StringProcessingRequester {

    public static final String FOO = "FOO";
    public static final String BAR = "BAR";
    public static final String QUIX = "QUIX";

    @Override
    public String processString(int i) throws IllegalArgumentException {
        if (i > 100 || i<0) {
            throw new IllegalArgumentException("Number must be between 0 and 100");
        }
        StringBuilder result = new StringBuilder();
        if (i % 3 ==0) {
            result.append(FOO);
        }
        if (i % 5 ==0) {
            result.append(BAR);
        }

        for (char c : String.valueOf(i).toCharArray()) {
            if (c == '3') {
                result.append(FOO);
            } else if (c == '5') {
                result.append(BAR);
            } else if (c == '7') {
                result.append(QUIX);
            }
        }


        return !result.isEmpty() ? result.toString() : String.valueOf(i);
    }

}
