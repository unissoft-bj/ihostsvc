/**
 * 
 */
package net.wyun.wm.audio;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import net.wyun.wm.service.IPLookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
    //@Value("${wm.audio.port}")
	//@Value("${wm.audio.frameNum}")
	private Integer frameNum = 60; // = 50; //cannot inject static variable
	
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
			aam = new AudioAgent(ip, userName, mac);
			//check dir
			this.fileService.checkUserDir(userName);
			agentMap.put(ip, aam);
		}
		
		return aam;
	}
	
	@PostConstruct
	public void init() {
		logger.info("start pkt service to process audio pkt");
		AudioAgent.setFrameNum(frameNum);
	}

}
