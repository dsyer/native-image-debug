package com.example.commandlinerunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.NativeHint;

@SpringBootApplication
@NativeHint(options = {"-g", "-O0"})
public class CommandlinerunnerApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(CommandlinerunnerApplication.class, args);
		Thread.currentThread().join(); // To be able to measure memory consumption
	}
	
}
