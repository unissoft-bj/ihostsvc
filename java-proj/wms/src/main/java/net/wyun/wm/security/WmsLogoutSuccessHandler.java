/**
 * 
 */
package net.wyun.wm.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author Xuecheng
 *
 */
@Component
public class WmsLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Authentication authentication) throws IOException, ServletException {
        if (authentication != null && authentication.getDetails() != null) {
            try {
                httpServletRequest.getSession().invalidate();
                logger.info("User {} Successfully Logout", authentication.getName());
                //you can add more codes here when the user successfully logs out,
                //such as updating the database for last active.
                
            } catch (Exception e) {
                e.printStackTrace();
                e = null;
            }
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.getWriter().flush();
        //redirect to login, no redirect for rest service
        //httpServletResponse.sendRedirect("/login");
    }
}

