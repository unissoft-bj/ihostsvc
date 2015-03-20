/**
 * 
 */
package net.wyun.wm.audio;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.wyun.wm.audio.utils.AudioUtil;

/**
 * @author Xuecheng
 * A service receives udp packet which contains audio signals. 
 * A background thread is dedicated to the server.
 * 
 */

@Component
public class AudioServer {

	private static final Logger logger = LoggerFactory.getLogger(AudioServer.class);
	public AudioServer() {
	   //needed by Spring to load the server.
	}

	public AudioServer(int port) {
		super();
		this.port = port;
	}
	
	@Autowired
	private PacketService packetService;


	private DatagramSocket serverSocket;
	private boolean contiune = true;
	
	//@Value("${wm.audio.port}")
	private int port = 8888;
	
	
	public void initSocket() throws IOException{
		//start udp server
		serverSocket = new DatagramSocket(null);
		serverSocket.setSoTimeout(20000);
		serverSocket.bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), port));

		System.out.println("start server on : ==>"  + port);
		
		
	}
	
	private static int frameSize = 8192; //4096;
	
	public void readStream() throws IOException{

		/**
		 * Formula for lag = (byte_size/sample_rate*2), sample_rate 11025, bandwidth is 176400bits/sec
		 * byte_size 9728, sample_rate 11025,  ~ 0.45 seconds lag. Voice slightly broken. 
		 * byte_size 8192, sample_rate 11025,  ~ 0.37 seconds lag. Voice extremely broken.
		 * byte_size 4000, sample_rate 11025   ~ 0.18 seconds lag. Voice slightly more broken than 9728.
		 * byte_size 1400, sample_rate 11025,  ~ 0.06 seconds lag. Voice extremely broken.
         * 
		 * frameNum 50;  file size ==> 400k; length 18  sec
		 * frameNum 500; file size ==> 4M;   length 180 sec, about 3 minutes
		 * frameNum 700; file size ==> 5.6M; length 252 sec, about 4 minutes and 12 sec
		 */

		byte[] receiveData = new byte[frameSize];
		int frameIndex = 0;
		while (contiune == true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			try{
				serverSocket.receive(receivePacket);
			}catch(SocketTimeoutException ste){
				System.out.println("read stream time out.");
				continue;
			}
			
			System.out.println((new Date()).toString() + ": Receive data: <==" + " length: " + receivePacket.getLength());
			
			//to Speaker, for dev usage only
			//AudioUtil.toSpeaker(receivePacket.getData());

			packetService.addPacket(receivePacket);
		}
		this.serverSocket.close();
	}
	
	@PreDestroy
	public void stopSvc(){
		this.contiune = false;
		cleanUp();
	}
	
    
    @PostConstruct
    public void startSvc() throws IOException {
		logger.info("start audio server.");
        Thread service = new Thread(() -> {
        	try {
        		initSocket();
				readStream();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
        });
        
        service.start();
    }
    
   
	public void cleanUp() {
    	
    }

	

	
}