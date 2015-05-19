/**
 * 
 */
package net.wyun.wm.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
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
	}
	
	

	@Autowired
	LotteryPhoneRecordRepository lotteryPhoneRecordRepo;
	
	List<LotteryPhoneRecord> lotteryPool = new ArrayList<LotteryPhoneRecord>();
	
	public List<String> getPhoneList(){
		if(lotteryTime == null) return new ArrayList<String>();
		
		lotteryPool = lotteryPhoneRecordRepo.findByBetweenStartTAndEndTAndSelected(lotteryTime.start_t, lotteryTime.end_t, false);
		Set<String> ss = new HashSet<String>();
		for(LotteryPhoneRecord lpr:lotteryPool){
			ss.add(lpr.getPhone());
		}
		
		return new ArrayList<String>(ss);
		
	}
	
	public List<String> getPhoneAll(){
		if(lotteryTime == null) return new ArrayList<String>();
		
		lotteryPool = lotteryPhoneRecordRepo.findByBetweenStartTAndEndTAndSelected(lotteryTime.start_t, lotteryTime.end_t, false);
		List<String> all = new ArrayList<String>();
		for(LotteryPhoneRecord lpr:lotteryPool){
			all.add(lpr.getPhone());
		}
		
		return all;
		
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
	
	
	public synchronized String pickUpLottery(){
		//based on lotteryPool
		int total = lotteryPool.size();
		if(total == 0) return "";
		
		Random ran = new Random();
		int ind = ran.nextInt(total);
		
		String lottery = lotteryPool.get(ind).getPhone();
		
		//get all lpr with number of lottery
		List<LotteryPhoneRecord> toBeRemoved = new ArrayList<LotteryPhoneRecord>();
		for(LotteryPhoneRecord lpr:lotteryPool){
			if(lpr.getPhone().equals(lottery)){
				toBeRemoved.add(lpr);
				lpr.setSelected(true);
				lotteryPhoneRecordRepo.save(lpr);
			}
		}
		
		//clean up
		for(LotteryPhoneRecord lpr:toBeRemoved){
			lotteryPool.remove(lpr);
		}
		
		return lottery;
	}
	
	Pattern phonePattern = Pattern.compile("\\d+");
	private boolean validPhoneNum(String phone){
		return phonePattern.matcher(phone).matches();
	}
	

}
