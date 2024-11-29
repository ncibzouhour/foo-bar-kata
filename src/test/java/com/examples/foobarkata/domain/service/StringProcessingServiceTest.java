package com.examples.foobarkata.domain.service;

import com.examples.foobarkata.domain.ports.api.IntProcessingRequester;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StringProcessingServiceTest {

    IntProcessingRequester intProcessingRequester;

    @BeforeAll
    public void setUp() {
        intProcessingRequester = new IntProcessingService();
    }

    @ParameterizedTest
    @MethodSource("provideStringsForFooBarProcessor")
    void processStringTest(int input, String expected)  {

        assertEquals(expected, intProcessingRequester.processIntToString(input));

    }

    @Test
    void processStringShouldThrowExceptionWhenNumberIsGt100(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                intProcessingRequester.processIntToString(101));

        assertEquals("Number must be between 0 and 100", exception.getMessage());

    }



    private static Stream<Arguments> provideStringsForFooBarProcessor() {
        return Stream.of(
                Arguments.of(0, "FOOBAR"),
                Arguments.of(1, "1"),
                Arguments.of(5, "BARBAR"),
                Arguments.of(3, "FOOFOO"),
                Arguments.of(7, "QUIX"),
                Arguments.of(51, "FOOBAR"),
                Arguments.of(53, "BARFOO"),
                Arguments.of(33, "FOOFOOFOO"),
                Arguments.of(15, "FOOBARBAR")
        );
    }

}