package com.examples.foobarkata.api.rest;


import com.examples.foobarkata.domain.ports.api.IntProcessingRequester;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/foobar")
@Slf4j
public class FooBarIntProcessorController {

    private final IntProcessingRequester intProcessingRequester;

    public FooBarIntProcessorController(IntProcessingRequester intProcessingRequester) {
        this.intProcessingRequester = intProcessingRequester;
    }

    @Operation(summary = "Process Int",
            description = "Process Int into a String. The response is the result String after processing foo bar rules")
    @GetMapping(value = "/process-int/{input}", produces = "plain/text")
    public String processInt(@PathVariable int input) {
        log.debug( "Calling /process-int/ with input : " + input);

        try {
            String result = intProcessingRequester.processIntToString(input);
            log.debug( input + "-->" + result);
            return result;
        } catch (IllegalArgumentException exc) {
            log.error("Error occurred : {}", exc.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        }
    }
}
