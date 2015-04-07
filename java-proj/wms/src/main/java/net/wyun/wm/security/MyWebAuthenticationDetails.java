package net.wyun.wm.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class MyWebAuthenticationDetails extends WebAuthenticationDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
	}
	
}
