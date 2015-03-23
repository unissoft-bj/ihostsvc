/**
 * 
 */
package net.wyun.wm.rest;

import java.security.Principal;

import net.wyun.wm.data.User;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xuecheng
 *
 */
@RestController
public class UserController {
	
	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
	

}
