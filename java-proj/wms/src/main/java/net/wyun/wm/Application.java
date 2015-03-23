/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
