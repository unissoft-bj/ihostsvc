/**
 * 
 */
package net.wyun.wm.audio;

/**
 * @author Xuecheng
 *
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PacketService extends Thread{

	private static final Logger logger = LoggerFactory.getLogger(PacketService.class);
	
    @Autowired
	AudioAgentFactory audioAgentFactory;
    
	private final static int QSIZE = 40;
	private BlockingQueue<DatagramPacket> pktQ = new ArrayBlockingQueue<DatagramPacket>(QSIZE);

	public boolean addPacket(DatagramPacket v) {
		return pktQ.offer(v);
	}

	public PacketService() {}

	boolean isShutDown = false;
	
	@Override
	public void run() {
		this.setName("pkt_service");
		execute();
	}

	
	//This part needs to be placed in a thread. If not, Spring context got blocked.
	private void execute() {
		while (!isShutDown) {
			try {
				DatagramPacket v = pktQ.poll(30, TimeUnit.SECONDS);
				if(null != v) {
					String ip = v.getAddress().getHostAddress();
					//logger.debug("pkt source ip {}", ip);
					AudioAgent aam = audioAgentFactory.getAgent(ip);
					aam.add(v);
				}
					
				//service.execute(new SmsTask(v));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}

		}

	}
	

	private void stopService() {
	}

	
	@PostConstruct
	public void init() {
		logger.info("start pkt service to process audio pkt");
		this.start();
	}
	

	
	@PreDestroy
	public void cleanUp() {
		logger.info("clean up before shut down pkt service.");
		isShutDown = true;
		while (!pktQ.isEmpty()) {
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		logger.info("shut down pkt service now.");
		stopService();
	}


}
