package com.examples.foobarkata;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info=@Info(title="FooBarKata Application"))
@SpringBootApplication
public class FoobarKataApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoobarKataApplication.class, args);
	}

}
