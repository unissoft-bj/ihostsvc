/**
 * 
 */
package net.wyun.wm.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.HttpAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;

/**
 * @author Xuecheng
 *
 */
@Component("authenticationProvider")
public class WmsAuthenticationProvider extends DaoAuthenticationProvider {
	
	private static final String PARA_PHONE = "phone";
	private static final String PARA_PHONE_PWD = "phone_pwd";
	
	private static final Logger logger = LoggerFactory.getLogger(WmsAuthenticationProvider.class);
	
	private UserCache userCache = new NullUserCache();

	@Autowired
	@Qualifier("userDetailsService")
	@Override
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}
	
    @SuppressWarnings("unchecked")
	@Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	
    	
		String username = (String) authentication.getPrincipal();  //mac
        String password = (String) authentication.getCredentials(); //mac_pwd
        
        logger.debug("authenticate user: {}", username);
        
        HttpAuthenticationToken token = (HttpAuthenticationToken) authentication;
        
        Optional<String> phone = Optional.fromNullable( token.getReqParmsMap().get(PARA_PHONE));
        Optional<String> phone_pass = Optional.fromNullable( token.getReqParmsMap().get(PARA_PHONE_PWD));
        
        Joiner.MapJoiner mapJoiner = Joiner.on(',').withKeyValueSeparator("=");
        System.out.println(mapJoiner.join(token.getReqParmsMap()));
        
        if(phone.isPresent()){
        	//go with phone/pwd
        	logger.debug("authenticate user: {} by phone", username);
        	return authenticateByPhone(authentication);
        }else{
        	//try mac/pwd
        	logger.debug("authenticate user: {} by mac", username);
        	return authenticateByMAC(authentication);
        	
        }
        
        
    }
    
    private Authentication authenticateByPhone(Authentication authentication) {

    	Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
                messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
                    "Only UsernamePasswordAuthenticationToken is supported"));
    	
    	   HttpAuthenticationToken token = (HttpAuthenticationToken) authentication;
    	   Optional<String> phone = Optional.fromNullable( token.getReqParmsMap().get(PARA_PHONE));
           Optional<String> phone_pwd = Optional.fromNullable( token.getReqParmsMap().get(PARA_PHONE_PWD));

            // Determine username (here still the mac)
            String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();

            boolean cacheWasUsed = true;
            UserDetails user = this.userCache.getUserFromCache(username);

            if (user == null) {
                cacheWasUsed = false;

                try {
                    user = retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
                } catch (UsernameNotFoundException notFound) {
                    logger.debug("User '" + username + "' not found");

                    if (hideUserNotFoundExceptions) {
                        throw new BadCredentialsException(messages.getMessage(
                                "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
                    } else {
                        throw notFound;
                    }
                }

                Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
            }
            
            //here is the real business to check phone/pw, need convert user to an Account object 

            try {
                //preAuthenticationChecks.check(user);
                //additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
            	if(!phone.isPresent() || !phone_pwd.isPresent() ){
            		throw new BadCredentialsException(messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            	}else{
            		//compare phone and pwd
            	}
            		
            	
            } catch (AuthenticationException exception) {
                if (cacheWasUsed) {
                    // There was a problem, so try again after checking
                    // we're using latest data (i.e. not from the cache)
                    cacheWasUsed = false;
                    user = retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
                    //preAuthenticationChecks.check(user);
                   // additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
                } else {
                    throw exception;
                }
            }

            //postAuthenticationChecks.check(user);

            if (!cacheWasUsed) {
                this.userCache.putUserInCache(user);
            }

            Object principalToReturn = user;

            /*
            if (forcePrincipalAsString) {
                principalToReturn = user.getUsername();
            }
            */

            return createSuccessAuthentication(principalToReturn, authentication, user);
        
	}

	private Authentication authenticateByMAC(Authentication authentication){
    	return super.authenticate(authentication);
    }
    
    /*
    @Override
    public boolean supports(Class<?> authentication) {
       return authentication.equals(HttpAuthenticationToken.class);
    }
    */


}