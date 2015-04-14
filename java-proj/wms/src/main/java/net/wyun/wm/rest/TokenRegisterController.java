/**
 * 
 */
package net.wyun.wm.rest;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.wyun.wm.data.RegisterRequest;
import net.wyun.wm.data.UserDto;
import net.wyun.wm.domain.MacAccount;
import net.wyun.wm.domain.MacAccountRepository;
import net.wyun.wm.domain.account.Account;
import net.wyun.wm.domain.account.AccountRepository;
import net.wyun.wm.domain.ihost.IHost;
import net.wyun.wm.domain.ihost.IHostRepository;
import net.wyun.wm.domain.mac.Mac;
import net.wyun.wm.domain.mac.MacRepository;
import net.wyun.wm.domain.token.Token;
import net.wyun.wm.domain.token.TokenRepository;
import net.wyun.wm.security.WmsAuthenticationProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

/**
 * @author Xuecheng
 * 
 * when the client call this service, it means the user don't have a valid:
 * 1. mac/mac-passwd (user automatically login)
 * 2. phone/pwd (the user will see the phone login page.
 *
 */
@RestController
public class TokenRegisterController {
	
	private static final Logger logger = LoggerFactory.getLogger(TokenRegisterController.class);
	String id;
	
	@Autowired
	TokenRepository tokenRepo;
	
	@Autowired
	MacRepository macRepo;
	
	@Autowired
	MacAccountRepository macAccountRepo;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	@Qualifier("wmsUserDetailsService")
	UserDetailsService userDetailsService;
	
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public UserDto register(@RequestBody RegisterRequest req, HttpServletResponse response) {
		UserDto ud = null;
		String macStr = req.getMac();
		String tokenStr = req.getToken();
		Token token = tokenRepo.findByToken(Integer.parseInt(tokenStr));
		
				
		if(null == token){
			//no token object found in database
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		if(token.isUsed()) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return null;
		}
		
		//check if token expires: now 24 hours
		Date create_t = token.getCreate_t();
		if(isTokenExpired(create_t)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		
		//update token with mac
		token.setMac(macStr);
		token.setUsed(true);
		tokenRepo.save(token);
		
		Mac mac = null;
		
		try{
			mac = (Mac) userDetailsService.loadUserByUsername(macStr);
		}catch(Exception e){
			e.printStackTrace();
			mac = null;
		}
		
		if(null == mac){
			mac = new Mac(macStr);
			mac.setEnabled(true);
		}
		
		String t_phone = token.getPhone();
		
		if(t_phone.isEmpty()){
            
			ud = new UserDto(mac); //for now
			
		}else{ //has a phone num
		   
		   if(mac.contains(t_phone)){
			   ud = new UserDto(mac);
		   }else{
			   //create association
			   MacAccount mac_account = new MacAccount();
			   mac_account.setMac(mac);
			   
			   Account accountFromPhone = accountRepo.findByPhone(t_phone);
			   if(accountFromPhone != null){
				   mac_account.setAccount(accountFromPhone);
			   }else{
				   //create new account with the phone
				   Account newAcct = new Account(t_phone);
				   newAcct.setOriginator(token.getAgent_id());
				   mac_account.setAccount(newAcct);
			   }
			   MacAccount updatedMA = macAccountRepo.save(mac_account);
			   Mac reloadedMac = (Mac) userDetailsService.loadUserByUsername(macStr); //did not get the newest account??
			  // Mac reloadedMac = updatedMA.getMac();
			   ud = new UserDto(reloadedMac);
			   
		   }
			   
		}
		
		return ud;
	}
	
	private static long Twenty4HOURS = 24 * 60 * 60 * 1000;
	private boolean isTokenExpired(Date create_t){
		Date now = new Date();
		long diff = now.getTime() - create_t.getTime();
		return diff > Twenty4HOURS;
	}
	
	
	
}
