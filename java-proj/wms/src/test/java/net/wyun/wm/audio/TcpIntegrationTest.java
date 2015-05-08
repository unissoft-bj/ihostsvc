package net.wyun.wm.audio;

import static org.junit.Assert.*;

import java.io.IOException;

import net.wyun.wm.audio.utils.AudioGenerator;

import org.junit.Test;

public class TcpIntegrationTest {

	@Test
	public void audioOverSockettest() throws IOException, InterruptedException {
		
		
		//start server
		TcpAudioServer server = startAudioServer();
		
		//sleep 4 sec
		TcpAudioServer.sleepQuietly(4);
		
		//start client
		TcpAudioClient client = new TcpAudioClient("localhost");
		System.out.println("send audio...");
		client.connectAudioServer();
		for(int i=0; i<5; i++){
			byte[] audio = AudioGenerator.generateAudio(1, 400 + 30*i);
			TcpAudioServer.sleepQuietly(1);
			client.sendBytes(audio);
		}
		
		
		//sleep 6 sec
		TcpAudioServer.sleepQuietly(2);
		System.out.println("stop test.");
		client.closeConnection();
		server.stop();
		
		
	}
	
	@Test  //for unit testing a server
	public void startServer() throws IOException{
		 TcpAudioServer server = new TcpAudioServer();
		 server.start();
		 
		 TcpAudioServer.sleepQuietly(50);
	}
	
	
	public TcpAudioServer startAudioServer() throws IOException{
		
        TcpAudioServer server = new TcpAudioServer();
		server.start();
		TcpAudioServer.sleepQuietly(2);
		return server;
		
	}
	
	@Test
	public void sendBytesTest() throws IOException, InterruptedException{
		// start client
		TcpAudioClient client = new TcpAudioClient("localhost");
		System.out.println("send audio...");
		client.connectAudioServer();
		for (int i = 0; i < 5; i++) {
			byte[] audio = AudioGenerator.generateAudio(1, 400 + 30 * i);
			TcpAudioServer.sleepQuietly(1);
			client.sendBytes(audio);
		}
		client.closeConnection();
	}

}
