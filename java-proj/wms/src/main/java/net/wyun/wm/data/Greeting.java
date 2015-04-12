/*
 * Copyright 2014 unisoft
 */

package net.wyun.wm.data;

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

	public Greeting(long id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}
