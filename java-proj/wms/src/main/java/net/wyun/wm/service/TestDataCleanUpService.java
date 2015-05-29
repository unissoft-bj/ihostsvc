package net.wyun.wm.service;

import java.util.Date;
import java.util.List;

import net.wyun.wm.domain.MacAccountRepository;
import net.wyun.wm.domain.account.AccountRepository;
import net.wyun.wm.domain.autoshow.AnswerRepository;
import net.wyun.wm.domain.autoshow.LotteryPhoneRecordRepository;
import net.wyun.wm.domain.autoshow.SurveyeeRepository;
import net.wyun.wm.domain.mac.Mac;
import net.wyun.wm.domain.mac.MacRepository;
import net.wyun.wm.rest.TokenRegisterController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestDataCleanUpService {
	
	private static final Logger logger = LoggerFactory.getLogger(TestDataCleanUpService.class);
	
	@Autowired
	MacRepository macRepo;
	
	@Autowired
	MacAccountRepository maRepo;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Transactional
	public void cleanMacAndAccount(Date cutDate){
		
		int num = maRepo.removeByCreate_tGreaterThan(cutDate);
		accountRepo.deleteByCreatetAfter(cutDate);
		macRepo.removeByCreate_tGreaterThan(cutDate);
		
		//macRepo.deleteByCreatetAfter(cutDate);
		logger.info("delete mac/account, total: {}", num);
	}
	
	@Autowired
	LotteryPhoneRecordRepository lotteryPhoneRecordRepo;
	@Autowired
	SurveyeeRepository surveyeeRepo;
	@Autowired
	AnswerRepository answerRepo;
	
	@Transactional
	public void cleanSurveyData(Date cutDate){
		
		
		answerRepo.deleteByCreatetAfter(cutDate);
		lotteryPhoneRecordRepo.deleteByCreatetAfter(cutDate);
		surveyeeRepo.deleteByCreatetAfter(cutDate);
		
		logger.info("delete survey data done.");
	}
	
	@Transactional
	public void cleanMacAndAccount1(int minutes){
		long duration = minutes*60*1000L;
		
		long cutoff = System.currentTimeMillis() - duration;
		
		Date cutDate = new Date(cutoff);
		
		System.out.println("cutoff date: " + cutDate.toString());
		
		List<Mac> lm =  macRepo.deleteByCreatetAfter(cutDate);
		
		for(Mac mac : lm){
			System.out.println("delete mac: " + mac.getMacInString());
		//	macRepo.delete(mac);
		}
		
		logger.info("Deleting Macs, number {}", lm.size() );
		
	}
	
	@Transactional
	public Mac delete(String id){
		logger.info("Deleting a todo entry with id: {}", id);

		Mac tmp = macRepo.findOne(id);
        logger.debug("Found Mac entry: {}", tmp);

        macRepo.delete(tmp);
        logger.info("Deleted Mac entry: {}", tmp);

        return tmp;
	}

	public void clean(Date cutDate) {
		this.cleanSurveyData(cutDate);
		this.cleanMacAndAccount(cutDate);
	}
	

}
