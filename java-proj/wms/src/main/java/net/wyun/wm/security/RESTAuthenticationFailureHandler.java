package net.wyun.wm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class RESTAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	CustomUserDetailsService userDetailsService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
	                                    AuthenticationException exception) throws IOException, ServletException {

		System.out.println("uds: " + this.userDetailsService.toString());
		//super.onAuthenticationFailure(request, response, exception);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().print("{'login': 'FAILURE', 'code': '1'}");
        response.getWriter().flush();
	}
}