/**
 * 
 */
package net.wyun.wm.rest;

import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import net.wyun.wm.data.User;
import net.wyun.wm.domain.ihost.IHostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xuecheng
 *
 */
@RequestMapping("/secure")
@RestController
public class UserController {/*implements ErrorController{

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error(HttpServletResponse response) {
    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "用户需登录。";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
	*/
	
	@RequestMapping("/user")
	public Principal user(Principal user) {
		System.out.println(user.toString());
		return user;
	}
	

}
