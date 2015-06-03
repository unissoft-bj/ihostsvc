/*
 * Copyright 2014 unisoft
 */

package net.wyun.wm;


import java.text.SimpleDateFormat;

import net.wyun.wm.util.CustomDateSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@ComponentScan(basePackages = {"net.wyun.wm"},
                  excludeFilters = @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "net.wyun.wm.audio.*"))
@EnableAutoConfiguration
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	public Application(){
		super();
		logger.info("initialize wms service now...");
		logger.info("set up global unhandled exception handler.");
		Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override public void uncaughtException(Thread t, Throwable e) {
                        logger.error(t.getName()+": "+e);
                    }
                });
	}
	
	@Bean
	public Jackson2ObjectMapperBuilder jacksonBuilder() {
	    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
	    builder.indentOutput(true).dateFormat(new SimpleDateFormat(CustomDateSerializer.WMS_DATE_FORMAT));
	    return builder;
	}
}
