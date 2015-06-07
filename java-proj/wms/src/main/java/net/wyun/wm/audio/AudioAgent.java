/**
 * 
 */
package net.wyun.wm.audio;

import java.io.IOException;
import java.net.DatagramPacket;

import net.wyun.wm.audio.utils.AudioUtil;
import net.wyun.wm.domain.reception.Reception;
import net.wyun.wm.domain.reception.ReceptionActivity;
import net.wyun.wm.service.ReceptionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Xuecheng
 *
 */
public class AudioAgent {
	private static final Logger logger = LoggerFactory.getLogger(AudioAgent.class);
	
	//need to construct user's audio directory
	public AudioAgent(String ip, String u, String mac) {
		this.ip = ip;
		this.agentName = u;
		this.mac = mac;
		
		//prepare the buffer for the wav file
		audioBytes = new byte[frameNum * frameSize];
	}
	
	private String ip;
	private String mac;
	private String agentName;
	
	private ReceptionService receptionSvc;
	
	public ReceptionService getReceptionSvc() {
		return receptionSvc;
	}

	public void setReceptionSvc(ReceptionService receptionSvc) {
		this.receptionSvc = receptionSvc;
	}

	byte[] audioBytes; 
	public static int frameSize = 1280; //8192; //4096;
	private static int frameNum = 400; //200; // = 50;
	
	private int frameIndex = 0;
	
	//flags to check if there is packet within last 30 seconds
	private boolean pktInLast30sec = false;

	public boolean isPktInLast30sec() {
		return pktInLast30sec;
	}

	public void setPktInLast30sec(boolean pktInLast30sec) {
		this.pktInLast30sec = pktInLast30sec;
	}
	
	public synchronized void checkEvery30Sec() throws IOException{
		if(frameIndex > 0 && !isPktInLast30sec()){
			logger.info("{} no pkt in 30 seconds, save audio to file", this.agentName);
			//save audio buffer to file
			byte[] newAudio = new byte[frameIndex * frameSize];
			System.arraycopy(audioBytes, 0, newAudio, 0, frameIndex * frameSize);
			
			persist(newAudio);
			frameIndex = 0;
			
		}
		setPktInLast30sec(false);
	}

	/**
	 * precondition that pkt's ip is the same as the member variable ip
	 * @param pkt
	 * @throws IOException
	 */
	public synchronized void add(DatagramPacket pkt) throws IOException{
		pktInLast30sec = true;
		
		byte[] newBytes = pkt.getData();
		System.arraycopy(newBytes, 0, audioBytes, 0 + frameIndex * frameSize, newBytes.length);
		frameIndex++;
		if(frameIndex == frameNum){
			//to file
			System.out.println("output to file now.");
			
			persist(audioBytes);
			
			frameIndex = 0;
		}
	}
	
	private void persist(byte[] bytes) throws IOException{
		try{
		long ts = this.toFile(audioBytes);
		ReceptionActivity ra = generateActivity(ts);
		receptionSvc.save(ra);
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
	}
	
	 public long toFile(byte[] bytes) throws IOException{
		 
		//if(frameIndex == 0) return;

		long ts = System.currentTimeMillis();
		String fileName = FileService.audioDir + this.agentName + "/"
				+ FileService.YMD + "/" + ts + ".wav";
		AudioUtil.toWAVFile(fileName, bytes);
		return ts;
	}
	 
	 String currentReceptionId;
	 /**
	  * in future might the reception id be saved somewhere in memory. to save the database ops. 
	  * @param ts
	  * @return
	  */
	 
	private ReceptionActivity generateActivity(long ts){
		ReceptionActivity ra = new ReceptionActivity();
		//String agent_id = this.receptionSvc.getAccountId(mac);
		Reception reception = receptionSvc.findReception(mac);
		
		String receptionId = "NOT FOUND";
		if(null != reception){
			receptionId = reception.getId();
		}else{
			logger.error("cannot find reception for mac {}, its file {}", mac, ts );
		}
		ra.setReception_id(receptionId);
		ra.setFile("" + ts);
		
		return ra;
	}

	public static void setFrameNum(int num) {
		frameNum = num;
	}
	 
}
