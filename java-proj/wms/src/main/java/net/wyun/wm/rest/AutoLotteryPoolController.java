/**
 * 
 */
package net.wyun.wm.rest;

import java.util.List;

import net.wyun.wm.domain.autoshow.LotteryTime;
import net.wyun.wm.service.AutoLotteryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class AutoLotteryPoolController {
	
	@Autowired
	AutoLotteryService autoLotteryService;
	
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	// get a set of phones
	//
	@RequestMapping(value="/lottery/time", method = {RequestMethod.POST})
	public void setLotteryTimeLimit(@RequestBody LotteryTime lotteryTime){
		autoLotteryService.setLotteryTime(lotteryTime);
	}
	
	@RequestMapping(value="/lottery", method = {RequestMethod.GET})
	public List<String> getPhoneList(){
		return autoLotteryService.getPhoneList();
	}
	
	@RequestMapping(value="/lottery", method = {RequestMethod.POST})
	public void addPhoneList(@RequestBody List<String> phones){
		autoLotteryService.addPhoneList(phones);
	}
	
	

}
