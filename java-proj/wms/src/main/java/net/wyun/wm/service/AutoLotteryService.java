/**
 * 
 */
package net.wyun.wm.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.wyun.wm.domain.autoshow.LotteryPhoneRecord;
import net.wyun.wm.domain.autoshow.LotteryPhoneRecordRepository;
import net.wyun.wm.domain.autoshow.LotteryTime;

/**
 * @author Xuecheng
 *
 */
@Service
public class AutoLotteryService {
	
	private static final Logger logger = LoggerFactory.getLogger(AutoLotteryService.class);
	
	private LotteryTime lotteryTime;
	
	public LotteryTime getLotteryTime() {
		return lotteryTime;
	}

	public void setLotteryTime(LotteryTime lotteryTime) {
		this.lotteryTime = lotteryTime;
		lotteryPool = lotteryPhoneRecordRepo.findByBetweenStartTAndEndTAndSelected(lotteryTime.start_t, lotteryTime.end_t, false);
	}
	
	

	@Autowired
	LotteryPhoneRecordRepository lotteryPhoneRecordRepo;
	
	List<LotteryPhoneRecord> lotteryPool = new ArrayList<LotteryPhoneRecord>();
	
	public List<String> getPhoneList(){
		Set<String> ss = new HashSet<String>();
		for(LotteryPhoneRecord lpr:lotteryPool){
			ss.add(lpr.getPhone());
		}
		
		return new ArrayList<String>(ss);
		
	}
	
	public void addPhoneList(List<String> phones){
		for(String phone:phones){
			if(!validPhoneNum(phone)){
				logger.warn("invalid phone number: " + phone);
				continue;
			}
			LotteryPhoneRecord lpr = new LotteryPhoneRecord();
			lpr.setPhone(phone);
			lotteryPhoneRecordRepo.save(lpr);
		}
		
	}
	
	Pattern phonePattern = Pattern.compile("\\d{10}");
	private boolean validPhoneNum(String phone){
		return phonePattern.matcher(phone).matches();
	}
	

}
