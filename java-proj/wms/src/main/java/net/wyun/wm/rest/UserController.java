/**
 * 
 */
package net.wyun.wm.rest;

import net.wyun.wm.data.UserDto;
import net.wyun.wm.domain.CurrentlyLoggedUser;
import net.wyun.wm.domain.mac.Mac;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xuecheng
 *
 */
@RequestMapping("/secure")
@RestController
public class UserController {
	
	@RequestMapping("/user")
	public UserDto user(@CurrentlyLoggedUser Mac mac) {
		System.out.println("MAC: " + mac.getMacInString());
		UserDto ud = new UserDto(mac);
		return ud;
	}
	

}
