/**
 * 
 */
package net.wyun.wm.domain.token;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import net.wyun.wm.domain.token.TokenRequest.UserRole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Xuecheng
 *
 */
@Component
public class AccountBootStrap {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountBootStrap.class);
	public AccountBootStrap() {
		logger.info("construct AccountBootStrap Service. It checks if the system is initialized properly.");
	}

	@Autowired
	TokenRepository tokenRepo;
	
	@PostConstruct
	public void checkTokenTable() throws IOException {
		int cnt = tokenRepo.findCount(); 
		if(cnt == 0){
			logger.info("create the bootstrap token for system now");
			
			Token first = new Token("2064916276", 2015168);
			first.setUser_role(UserRole.ROLE_MANAGER);
			tokenRepo.save(first);
		}
		
		if(cnt == 1){
			Iterable<Token> ts = tokenRepo.findAll();
			for(Token t:ts){
				if(t.isUsed()) {
					logger.info("token has been used.");
					break;
				}
				
				t.setCreate_t(new Date());
				tokenRepo.save(t);
			}
		}
	}
	

}
