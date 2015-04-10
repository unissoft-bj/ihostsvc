package net.wyun.wm.security;

import net.wyun.wm.domain.mac.Mac;

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
	
	private static final String MSG_PHONE_PAGE = "{'login': 'FAILURE', 'code': 'PHONE_LOGIN'}";
	private static final String MSG_TOKEN_PAGE = "{'login': 'FAILURE', 'code': 'TOKEN_LOGIN'}";
	
	@Autowired
	WmsUserDetailsService userDetailsService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
	                                    AuthenticationException exception) throws IOException, ServletException {

		System.out.println("uds: " + this.userDetailsService.toString());
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		String userName = request.getParameter("username");  //mac address
		Mac mac = (Mac) userDetailsService.loadUserByUsername(userName);
		if(mac.getAccount() != null && !mac.getAccount().getPassword().isEmpty()){
			response.getWriter().print(MSG_PHONE_PAGE);
		}else{
			response.getWriter().print(MSG_TOKEN_PAGE);
		}
		
        response.getWriter().flush();
	}
}