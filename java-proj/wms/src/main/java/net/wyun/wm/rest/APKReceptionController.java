/**
 * 
 */
package net.wyun.wm.rest;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import net.wyun.wm.data.ReceptionRequest;
import net.wyun.wm.domain.CurrentlyLoggedUser;
import net.wyun.wm.domain.account.Account;
import net.wyun.wm.domain.mac.Mac;
import net.wyun.wm.domain.reception.Reception;
import net.wyun.wm.domain.reception.ReceptionRepository;
import net.wyun.wm.security.WmsUserDetailsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is used by android apk to create a reception
 * @author Xuecheng
 *
 */
@RestController
public class APKReceptionController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReceptionController.class);
	
	@Autowired
	ReceptionRepository receptionRepo;
	
	@Autowired
	WmsUserDetailsService userDetailsSvc;
	
	@RequestMapping(value="/reception", method = {RequestMethod.POST})
	Reception createUpdateReception(@RequestBody ReceptionRequest receptionRequest, 
			                        HttpServletResponse response){
		
		String phone = receptionRequest.getPhone();
		String phoneMac = receptionRequest.getMac();
		
		Mac mac = (Mac) userDetailsSvc.loadUserByUsername(phoneMac);
		
		//in production check mac: TODO
		
			
	    if(mac == null){
				logger.error("illegal operation, mac: {}, phone: {}", phoneMac, phone);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				throw new RuntimeException("reception cannot be created. illegal operation.");
		}
		
	    Account acct = mac.getAccount();
	    if(acct == null){
	    	logger.error("no account available, mac: {}, phone: {}", phoneMac, phone);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			throw new RuntimeException("reception cannot be created. illegal operation.");
	    }
	    
	    //check phone
	    if(!phone.equals(acct.getPhone())){
	    	logger.warn("phone is not matched, from user: " + phone + ", db: " + acct.getPhone());
	    }
		
	    Reception reception = new Reception();
		reception.setAgent_id(acct.getId());
		response.setStatus(HttpServletResponse.SC_CREATED);
		
		return receptionRepo.save(reception);
		
	}
	
	
}
