/**
 * 
 */
package net.wyun.wm.security;


import net.wyun.wm.domain.mac.MacAddressLookupService;
import net.wyun.wm.service.InternetGuard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	private static final Logger logger = LoggerFactory.getLogger(WmsLogoutSuccessHandler.class);
	
	@Autowired
	@Qualifier("internetGuardImpl")
	InternetGuard internetGuard;
	
	 @Autowired
	    @Qualifier("macAddrLookupService")
	    MacAddressLookupService lookupSvc;
	
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Authentication authentication) throws IOException, ServletException {
    	String ip = httpServletRequest.getRemoteAddr();
        if (authentication != null && authentication.getDetails() != null) {
            try {
                httpServletRequest.getSession().invalidate();
                
                String mac = lookupSvc.getMacAddrByIP(ip);
                internetGuard.logoff(mac);
                
                logger.info("User {} Successfully Logout", authentication.getName());
                //you can add more codes here when the user successfully logs out,
                //such as updating the database for last active.
                
            } catch (Exception e) {
                e.printStackTrace();
                e = null;
            }
        }

        logger.info("User {} Logout done", authentication.getName());
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.getWriter().flush();
        //redirect to login, no redirect for rest service
        //httpServletResponse.sendRedirect("/login");
    }
}

