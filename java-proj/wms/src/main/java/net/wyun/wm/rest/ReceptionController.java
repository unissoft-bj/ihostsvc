/**
 * 
 */
package net.wyun.wm.rest;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import net.wyun.wm.domain.CurrentlyLoggedUser;
import net.wyun.wm.domain.account.Account;
import net.wyun.wm.domain.mac.Mac;
import net.wyun.wm.domain.reception.Reception;
import net.wyun.wm.domain.reception.ReceptionRepository;
import net.wyun.wm.security.CustomUserDetailsService;

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
 * @author Xuecheng
 *
 */
@RequestMapping("/secure")
@RestController
public class ReceptionController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReceptionController.class);
	
	@Autowired
	ReceptionRepository receptionRepo;
	
	@RequestMapping(value="/reception", method = {RequestMethod.POST})
	Reception createUpdateReception(@RequestBody Reception reception, 
			                        @CurrentlyLoggedUser Mac mac,
			                        HttpServletResponse response){
		
		String acct_id = reception.getAgent_id();
		
		if(reception.getId() != null){
			//update
			reception.setModify_t(new Date());
			//check if current user can update the reception or not
			
			if(!mac.getAccount().getId().equals(acct_id)){
				logger.error("illegal update, mac: {}, account_id: {}", mac.getMacInString(), acct_id);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				throw new RuntimeException("reception cannot be updated. illegal operation.");
			}
			
		}else{
			Account acct = mac.getAccount();
			reception.setAgent_id(acct.getId());
			response.setStatus(HttpServletResponse.SC_CREATED);
		}
		
		
		return receptionRepo.save(reception);
		
	}
	
	@RequestMapping(value="/reception/{id}", method = {RequestMethod.GET})
	Reception getReception(@PathVariable("id") long id){
		return receptionRepo.findOne(id);
	}
	

}
