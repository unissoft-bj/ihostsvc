/**
 * 
 */
package net.wyun.wm.rest;

import net.wyun.wm.domain.CurrentlyLoggedUser;
import net.wyun.wm.domain.account.Account;
import net.wyun.wm.domain.account.AccountRepository;
import net.wyun.wm.domain.mac.Mac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xuecheng
 *
 */
@RequestMapping("/secure")
@RestController
public class AccountController {
	
	@Autowired
	AccountRepository acctRepo;
	
	@RequestMapping(value= "/password/{pw}", method=RequestMethod.POST)
	Account setPassword(@PathVariable("pw") String pwd, @CurrentlyLoggedUser Mac mac){
		Account acct = mac.getAccount();
		acct.setPassword(pwd);
		acct = acctRepo.save(acct);
		return acct;
	}

}
