/*
 * Copyright 2014 unisoft.
 *

 */

package net.wyun.wm.security;

import net.wyun.wm.domain.mac.Mac;
import net.wyun.wm.domain.mac.MacAddressUtil;
import net.wyun.wm.domain.mac.MacRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("wmsUserDetailsService")
public class WmsUserDetailsService implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(WmsUserDetailsService.class);
    private final MacRepository macRepository;

    @Autowired
    public WmsUserDetailsService(MacRepository macRepository) {
        this.macRepository = macRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	logger.info("check user (mac): {}", username);
    	
    	//first validation of mac
    	if(!MacAddressUtil.isValidMac(username))
    		throw new UsernameNotFoundException(String.format("Mac %s is not a valid mac address!", username));
    	
    	//convert it to a valid Long
    	Long macAddress = MacAddressUtil.toLong(username);
    	
        Mac mac = macRepository.findByMac(macAddress);
        if(mac == null) {
            throw new UsernameNotFoundException(String.format("Mac %s not exist in system!", username));
        }
        
        return mac;
    }


    
}
