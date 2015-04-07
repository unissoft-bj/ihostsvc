/**
 * 
 */
package net.wyun.wm.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

/**
 * @author Xuecheng
 *
 */
@Component("authDetailsSource")
public class WebAuthenticationDetailsSourceImpl
		implements
		AuthenticationDetailsSource<HttpServletRequest, MyWebAuthenticationDetails> {

	@Override
	public MyWebAuthenticationDetails buildDetails(HttpServletRequest context) {
		// the constructor of MyWebAuthenticationDetails can retrieve
		// all extra parameters given on a login form from the request
		// MyWebAuthenticationDetails is your LoginDetails class
		return new MyWebAuthenticationDetails(context);
	}
	
}
