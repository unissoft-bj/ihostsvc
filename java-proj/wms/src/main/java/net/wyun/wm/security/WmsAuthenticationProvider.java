/**
 * 
 */
package net.wyun.wm.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.HttpAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;

/**
 * @author Xuecheng
 *
 */
@Component("authenticationProvider")
public class WmsAuthenticationProvider extends DaoAuthenticationProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(WmsAuthenticationProvider.class);

	@Autowired
	@Qualifier("userDetailsService")
	@Override
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}
	
    @SuppressWarnings("unchecked")
	@Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	
    	
		String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        
        logger.debug("username: {}", username);
        
        HttpAuthenticationToken token = (HttpAuthenticationToken) authentication;
        
        //String phone = token.getPhone();
        //String phone_pass = token.getPhone_pass();
        System.out.println(token.toString());
        Joiner.MapJoiner mapJoiner = Joiner.on(',').withKeyValueSeparator("=");
        System.out.println(mapJoiner.join(token.getReqParmsMap()));
        

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            throw new BadCredentialsException("Invalid Domain User Credentials");
        }
        
        try {
        	 
    		Authentication auth = super.authenticate(authentication);
    		
     
    		//if reach here, means login success, else an exception will be thrown
    		//reset the user_attempts
    	
     
    		return auth;
     
    	  } catch (BadCredentialsException e) {	
     
    		//invalid login, update to user_attempts
    		throw e;
     
    	  }
        
    }
    
    /*
    @Override
    public boolean supports(Class<?> authentication) {
       return authentication.equals(HttpAuthenticationToken.class);
    }
    */


}