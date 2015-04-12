/*
 * Copyright 2014 Unisoft
 */

package net.wyun.wm.rest;

import java.util.concurrent.atomic.AtomicLong;

import net.wyun.wm.data.Greeting;
import net.wyun.wm.domain.mac.Mac;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/secure")
@RestController
public class ResourceController {

	private static final String template = "欢迎使用Matrix WIFI, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping("/resource")
	public Greeting greeting(@AuthenticationPrincipal Mac mac) {
		System.out.println("get resource here: " + mac.getMacInString());
		return new Greeting(counter.incrementAndGet(), String.format(template, mac.getMacInString()));
	}

}
