/**
 * 
 */
package net.wyun.wm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * @author Xuecheng
 *
 */
@Component("authenticationProvider")
public class UserDetailsAuthenticationProviderImpl extends DaoAuthenticationProvider {

	@Autowired
	@Qualifier("userDetailsService")
	@Override
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}
	
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MyWebAuthenticationDetails detais = (MyWebAuthenticationDetails) authentication.getDetails();
        // verify the authentication details here !!!
        // and return proper authentication token (see DaoAuthenticationProvider for example)
        
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


}