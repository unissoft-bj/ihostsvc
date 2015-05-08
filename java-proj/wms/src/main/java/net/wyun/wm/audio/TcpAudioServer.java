/**
 * 
 */
package net.wyun.wm.audio;

/**
 * @author Xuecheng
 *
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import net.wyun.wm.audio.utils.AudioUtil;

/**
 * 
 * @author Xuecheng
 *
 */

@Component
public class TcpAudioServer {

	AsynchronousServerSocketChannel listener;
	
	

	private void init() throws IOException {
		listener = AsynchronousServerSocketChannel.open().bind(
				new InetSocketAddress(8001));
		System.out.println("server now is listening to " + 8001);

	}

	public TcpAudioServer() throws IOException {
		init();
	}

	public void start() {

		// Listen for a new request
		listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
			        
					
			        /////// the socket server not working if allocate buffer here
			        // Allocate a byte buffer (8K) to read from the client
					//private ByteBuffer byteReadBuffer = ByteBuffer.allocate(8192);

					@Override
					public void completed(AsynchronousSocketChannel ch, Void att) {
						 listener.accept(null, this);
						try {
							System.out.println("new connection: " + ch.getRemoteAddress());
						} catch (IOException e1) {			}

						
						ByteBuffer byteReadBuffer = ByteBuffer
								.allocate(8192);
						int bytesRead = 0;
						
						while (true) {
							
							bytesRead = this.readChannel(ch, byteReadBuffer);
							System.out.println("bytes read: " + bytesRead);
							
							byteReadBuffer.flip(); //for future get or put
							if(bytesRead == 0) {
								TcpAudioServer.sleepQuietly(1);
								/*
								try {
									write2Channel(ch);
								} catch (InterruptedException
										| ExecutionException e) {
									
								}
								*/
								continue;
							}else if(bytesRead < 0){
								
								break;
							}else{
								// Convert the buffer into a line
								byte[] lineBytes = new byte[bytesRead];
								byteReadBuffer.get(lineBytes, 0, bytesRead);

								// Debug
								System.out.println("Message to speaker");

								AudioUtil.toSpeaker(lineBytes);
								byteReadBuffer.clear();
							}

						}

						System.out.println("End of conversation");
						TcpAudioServer.closeChannel(ch);
					}
					
					private int write2Channel(AsynchronousSocketChannel ch) throws InterruptedException, ExecutionException{
						int nBytes = 0;
						byte[] bytes = " ".getBytes();
						ByteBuffer writeBBuf = ByteBuffer.wrap(bytes);
				        while(writeBBuf.hasRemaining()){
				            Future<Integer> future = ch.write(writeBBuf);
				            nBytes += future.get();
				        }

				        writeBBuf.rewind();
				        return nBytes;
					}
					
					private int readChannel(AsynchronousSocketChannel ch, ByteBuffer byteReadBuffer) {
						int bytesRead = 0;
						try {
							Future<Integer> future = ch.read(byteReadBuffer);
							while (!future.isDone()) {
							}
							bytesRead = future.get();

						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}

						return bytesRead;
					}

					@Override
					public void failed(Throwable exc, Void att) {
						// /...
					}
				});
		
		System.out.println("tcp audio server is up.");
	}
	
	public void stop() throws IOException{
		System.out.println("close server socket channel. audio server shutdown.");
		this.listener.close();
	}
	
	
	@PostConstruct
	public void startSvc() throws IOException {
		this.start();
	}
	
	@PreDestroy
	public void stopSvc() throws IOException{
		this.stop();
		this.listener = null;
	}
	
	static void closeChannel(AsynchronousSocketChannel ch){
		if (null != ch && ch.isOpen()) {
			try {
				ch.close();
			} catch (IOException e) {}
			
			ch = null;
		}
	}
	
	
	
	static void sleepQuietly(int secs)
	{
		try {
			TimeUnit.SECONDS.sleep(secs);
		} catch (InterruptedException e) {	}
	}

	
}