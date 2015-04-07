/**
 * 
 */
package net.wyun.wm.audio;

import java.io.IOException;
import java.net.DatagramPacket;

import net.wyun.wm.audio.utils.AudioUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Xuecheng
 *
 */
public class AudioAgent {
	
	
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
	
	byte[] audioBytes; 
	private static int frameSize = 8192; //4096;
	private static int frameNum = 200; // = 50;
	
	private int frameIndex = 0;
	
	/**
	 * precondition that pkt's ip is the same as the member variable ip
	 * @param pkt
	 * @throws IOException
	 */
	public void add(DatagramPacket pkt) throws IOException{
		
		byte[] newBytes = pkt.getData();
		System.arraycopy(newBytes, 0, audioBytes, 0 + frameIndex * frameSize, newBytes.length);
		frameIndex++;
		if(frameIndex == frameNum){
			//to file
			System.out.println("output to file now.");
			frameIndex = 0;
			this.toFile();
		}
	}
	
	 public void toFile() throws IOException{
	    	long ts = System.currentTimeMillis();
	    	String fileName = FileService.audioDir + this.agentName + "/" + FileService.YMD + "/" +  ts + ".wav";
			AudioUtil.toWAVFile(fileName, audioBytes);
	}

	public static void setFrameNum(int num) {
		frameNum = num;
	}
	 
}
