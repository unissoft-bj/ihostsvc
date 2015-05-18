/**
 * 
 */
package net.wyun.wm.rest;

import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wyun.wm.data.Survey;
import net.wyun.wm.data.UserDto;
import net.wyun.wm.domain.MacAccount;
import net.wyun.wm.domain.MacAccountRepository;
import net.wyun.wm.domain.account.Account;
import net.wyun.wm.domain.account.AccountRepository;
import net.wyun.wm.domain.autoshow.Answer;
import net.wyun.wm.domain.autoshow.AnswerRepository;
import net.wyun.wm.domain.autoshow.Surveyee;
import net.wyun.wm.domain.autoshow.SurveyeeRepository;
import net.wyun.wm.domain.mac.Mac;
import net.wyun.wm.domain.mac.MacRepository;
import net.wyun.wm.domain.role.Role;
import net.wyun.wm.domain.role.RoleRepository;
import net.wyun.wm.domain.token.TokenRepository;
import net.wyun.wm.domain.token.TokenRequest.UserRole;
import net.wyun.wm.service.InternetGuard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Xuecheng
 * 
 * when the client call this service, it means the user don't have a valid:
 * 1. collect surveyee info., and save question answers.
 * 2. send back mac/mac-pass
 *
 */
@RestController
public class SurveyController {
	
	private static final Logger logger = LoggerFactory.getLogger(SurveyController.class);
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
	RoleRepository roleRepo;
	
	@Autowired
	@Qualifier("wmsUserDetailsService")
	UserDetailsService userDetailsService;
	
	@Autowired
	@Qualifier("internetGuardImpl")
	InternetGuard internetGuard;
	
	
	@RequestMapping(value="/survey", method=RequestMethod.POST)
	public UserDto register(@RequestBody Survey survey, HttpServletRequest request, HttpServletResponse response) {
		UserDto ud = null;
		String macStr = survey.getMac();
		
		//open internet access
		this.internetGuard.authorize(request.getRemoteAddr());
		
		Surveyee surveyee = survey.getSurveyee();
		
		String t_phone = surveyee.getPhone().trim();
		logger.info("surveyee phone#: " + t_phone);
		
		//save survey
		saveSurvey(survey);
		
		//
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
		}else{
			logger.info("existing mac user: " + macStr);
		}
		
		// always ROLE_USER
		UserRole ur = UserRole.ROLE_USER;
		Role role = roleRepo.findByName(ur.toString());
		
		if (mac.contains(t_phone)) {
			logger.info("existing account with mac " + t_phone + ", " + macStr);
			mac.getAccount().addRole(role);
			mac = this.macRepo.save(mac);
			ud = new UserDto(mac);
		} else {
			// create new m_a association
			MacAccount mac_account = new MacAccount();
			mac_account.setMac(mac);

			Account accountFromPhone = accountRepo.findByPhone(t_phone);
			if (accountFromPhone != null) {
				mac_account.setAccount(accountFromPhone);
				accountFromPhone.addRole(role);
				//mac = this.macRepo.save(mac);
				
			} else {
				// create new account with the phone
				Account newAcct = new Account(t_phone);
				newAcct.setOriginator("SURVEY");
				newAcct.addRole(role);
				Account savedNewAcct = accountRepo.save(newAcct);
				mac_account.setAccount(savedNewAcct);
			}
			
			MacAccount updatedMA = macAccountRepo.save(mac_account);
			//Mac reloadedMac = (Mac) userDetailsService.loadUserByUsername(macStr); // did not get the newest
													// account?? it is likely cached
			Mac reloadedMac = macRepo.findOne(mac.getId());
			//or still use mac
			//Set<MacAccount> maset = mac.getMacAccounts();
			//maset.add(updatedMA);
			//Mac uMac = macRepo.save(mac);
			
			// Mac reloadedMac = updatedMA.getMac();
			ud = new UserDto(reloadedMac);

		}
			   
		return ud;
	}
	
	@Autowired
	SurveyeeRepository surveyeeRepo;
	
	@Autowired
	AnswerRepository answerRepo;
	
	@Transactional
	private void saveSurvey(Survey survey){
		Answer[] ans = survey.getAnswers();
		Surveyee surveyee = survey.getSurveyee();
		if(surveyee.getCreate_t() == null){
			surveyee.setCreate_t(new Date());
		}
		
		//save surveyee
		Surveyee ss = surveyeeRepo.save(surveyee);
		for(Answer an:ans){
			an.setSurveyee_id(ss.getId());
			answerRepo.save(an);
		}
		
	}
	
}
