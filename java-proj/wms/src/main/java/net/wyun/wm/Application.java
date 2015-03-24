/*
 * Copyright 2014 unisoft
 */

package net.wyun.wm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("net.wyun")
@EnableAutoConfiguration
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	public Application(){
		super();
		logger.info("initialize wlsp service now...");
		logger.info("set up global unhandled exception handler.");
		Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override public void uncaughtException(Thread t, Throwable e) {
                        logger.error(t.getName()+": "+e);
                    }
                });
	}
}
