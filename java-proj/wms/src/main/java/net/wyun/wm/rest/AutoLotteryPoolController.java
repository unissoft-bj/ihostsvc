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
 * for method access control, refer to:
 * @link http://kielczewski.eu/2014/12/spring-boot-security-application/
 *
 */

@RequestMapping("/secure")
@RestController
public class AutoLotteryPoolController {
	
	@Autowired
	AutoLotteryService autoLotteryService;
	
	@RequestMapping(value="/lottery/time", method = {RequestMethod.POST})
	public void setLotteryTimeLimit(@RequestBody LotteryTime lotteryTime){
		autoLotteryService.setLotteryTime(lotteryTime);
	}
	
	
	@RequestMapping(value="/lottery", method = {RequestMethod.GET})
	public List<String> getPhoneList(){
		return autoLotteryService.getPhoneList();
	}
	
	@RequestMapping(value="/lottery/all", method = {RequestMethod.GET})
	public List<String> getPhoneAll(){
		return autoLotteryService.getPhoneAll();
	}
	
	@RequestMapping(value="/lottery", method = {RequestMethod.POST})
	public void addPhoneList(@RequestBody List<String> phones){
		autoLotteryService.addPhoneList(phones);
	}
	
	@RequestMapping(value="/lottery/draw", method = {RequestMethod.GET})
	public String getLotteryNum(){
		return autoLotteryService.pickUpLottery();
	}
	
	

}
