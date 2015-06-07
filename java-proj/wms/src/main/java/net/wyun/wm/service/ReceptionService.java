/**
 * 
 */
package net.wyun.wm.service;

import java.util.Date;
import java.util.List;

import net.wyun.wm.domain.account.Account;
import net.wyun.wm.domain.mac.Mac;
import net.wyun.wm.domain.mac.MacAddressUtil;
import net.wyun.wm.domain.mac.MacRepository;
import net.wyun.wm.domain.reception.Reception;
import net.wyun.wm.domain.reception.ReceptionActivity;
import net.wyun.wm.domain.reception.ReceptionActivityRepository;
import net.wyun.wm.domain.reception.ReceptionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author michael
 *
 */
@Service
public class ReceptionService {
	
	@Autowired
	ReceptionRepository receptionRepo;
	@Autowired
	ReceptionActivityRepository activityRepo;
	@Autowired
	MacRepository macRepo;
	
	public Reception save(Reception reception){
		return receptionRepo.save(reception);
	}
	
	public Reception getById(String id){
		return receptionRepo.findOne(id);
	}
	
	public ReceptionActivity save(ReceptionActivity activity){
		return activityRepo.save(activity);
	}
	
	/**
	 * 
	 * @param macStr
	 * @return an empty String or
	 *         a valid acctId (UUID)
	 */
	public String getAccountId(String macStr){
        Long macAddress = MacAddressUtil.toLong(macStr);
    	
        Mac mac = macRepo.findByMac(macAddress);
        
        Account acct = mac.getAccount();
        
        String acctId = "";
        if(acct != null){
        	acctId = acct.getId();
        }
        
        return acctId;
	}
	
	private Date minutesAgo(int minutes){
		long duration = minutes * 60 * 1000L;
		long cutoff = System.currentTimeMillis() - duration;
		Date cutDate = new Date(cutoff);
		return cutDate;
	}
	/**
	 * find within 240  minute (4hours) the latest Reception, could be null
	 * @param macStr
	 * @return
	 */
	public Reception findReception(String macStr) {
		
		Date cutDate = minutesAgo(240);

		String acctId = this.getAccountId(macStr);
		
		List<Reception> lr = this.receptionRepo.findByCreatetAfterAndAgentIdOrderByCreatetDesc(cutDate, acctId);
		
        if(lr != null && !lr.isEmpty()){
        	return lr.get(0);
        }
        
        return null;
	}
	
	
	
	
	

}
