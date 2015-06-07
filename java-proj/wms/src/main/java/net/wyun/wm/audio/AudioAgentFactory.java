/**
 * 
 */
package net.wyun.wm.audio;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import net.wyun.wm.domain.reception.ReceptionRepository;
import net.wyun.wm.service.IPLookup;
import net.wyun.wm.service.ReceptionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Xuecheng
 *
 */
@Component
public class AudioAgentFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(AudioAgentFactory.class);
	private HashMap<String, AudioAgent> agentMap = new HashMap<String, AudioAgent>();
	
	@Autowired
	IPLookup ipLookup;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	ReceptionService receptionSvc;
	
    //@Value("${wm.audio.port}")
	//@Value("${wm.audio.frameNum}")
	private Integer frameNum = 1000; // = 50; //cannot inject static variable
	
	public AudioAgentFactory(){}
	
	public AudioAgent getAgent(String ip){
		
		AudioAgent aam;
		if(agentMap.containsKey(ip)){
			aam = agentMap.get(ip);
			
		}else{
			//start a new agent manager, also mkdir for it
			logger.info("create agent for ip" + ip);
			String userName = ipLookup.getUserName(ip);
			String mac = ipLookup.getMAC(ip);
			logger.info("find mac {}", mac);
			aam = new AudioAgent(ip, userName, mac);
			//check dir
			this.fileService.checkUserDir(userName);
			aam.setReceptionSvc(receptionSvc);
			agentMap.put(ip, aam);
		}
		
		return aam;
	}
	
	@Scheduled (cron= "*/30 * * * * *") // every 30 seconds
	public void checkAudioRecording() throws IOException{
		//check if audio stream is ongoing
		logger.info("check audioagent if it has no incoming pkt in last 30 seconds");
		for(String ip : agentMap.keySet()){
			logger.info("check audioagent {}", ip);
			AudioAgent aa = agentMap.get(ip);
			aa.checkEvery30Sec();
		}
		
	}
	
	@PostConstruct
	public void init() {
		logger.info("start pkt service to process audio pkt");
		AudioAgent.setFrameNum(frameNum);
	}

}
