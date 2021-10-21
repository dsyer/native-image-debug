package com.example.commandlinerunner;

import java.util.Arrays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CLR implements CommandLineRunner {

	private static final Log logger = LogFactory.getLog(SpringApplication.class);

	public CLR(ConfigurableApplicationContext context) {
		ConfigurableApplicationContext context1 = context;
		Arrays.stream(context1.getBeanDefinitionNames()).forEach(s -> System.out.println(s));
	}

	@Override
	public void run(String... args) throws Exception {
		int i = 0;
		System.out.println("commandlinerunner running! " + (i++));
		logger.trace("WARNING log message " + (i++));
		logger.debug("DEBUG log message " + (i++));
		logger.info("INFO log message " + (i++));
		logger.warn("WARNING log message " + (i++));
		logger.error("ERROR log message " + (i++));
	}
	
}
