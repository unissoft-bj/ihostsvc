/*
 * Copyright 2014 unisoft
 */

package net.wyun.wm.data;

import java.util.Date;

import javax.persistence.Entity;

/**
 * Class that save the customized welcome messages on the client side.
 * @author Xuecheng
 *
 */

//@Entity  TODO
public class Greeting {

	private final long id;
	private final String content;
	private Date now;

	public Date getNow() {
		return now;
	}

	public void setNow(Date now) {
		this.now = now;
	}

	public Greeting(long id, String content) {
		this.id = id;
		this.content = content;
		this.now = new Date();
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}
