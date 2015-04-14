/**
 * 
 */
package net.wyun.wm.rest;

import java.util.Random;

import net.wyun.wm.data.UserDto;
import net.wyun.wm.domain.CurrentlyLoggedUser;
import net.wyun.wm.domain.mac.Mac;
import net.wyun.wm.domain.token.Token;
import net.wyun.wm.domain.token.TokenRepository;
import net.wyun.wm.domain.token.TokenRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
public class TokenController {
	
	@Autowired
	TokenRepository tokenRepo;
	
	@RequestMapping(value="/shangwangma/tr", method = {RequestMethod.GET})
	public TokenRequest getTokenReq() {
		TokenRequest tr = new TokenRequest();
		tr.setPhone("1380796221");
		tr.setType(TokenRequest.Type.sms);
		tr.setUserRole(TokenRequest.UserRole.ROLE_SERVICE);
		
		return tr;
	}
	
	@RequestMapping(value="/shangwangma", method = {RequestMethod.POST})
	public Token getToken(@CurrentlyLoggedUser Mac mac,  @RequestBody TokenRequest tr) {
		
		Random ran = new Random();
		//generate token (6 digit)
		Integer r_n = 100000 + ran.nextInt(900000);
		
		System.out.println("" + r_n + ", " + tr.getPhone() + tr.getUserRole());
		
		String phone = tr.getPhone(); //could be null or empty? do we need validate it?
		if(phone == null) phone = "";
		
		Token tk = new Token(phone, r_n);
		tk.setAgent_id(mac.getAccount().getId());
		
		this.tokenRepo.save(tk);
		
		return tk;
	}
	

}
