package net.wyun.wm.audio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class TcpAudioClient {
	
	private String serverAddr;
	
	public TcpAudioClient(String server){serverAddr = server;}
	
	private SocketChannel client;
	
    public void connectAudioServer() throws IOException, InterruptedException {

        client = SocketChannel.open();
        client.configureBlocking(false);
        InetSocketAddress isa = new InetSocketAddress(serverAddr, 8001);
        client.connect(isa);

        while(! client.finishConnect() ){
            TimeUnit.SECONDS.sleep(1);       }  //wait, or do something else...
    }

    public void closeConnection() throws IOException {
        client.close();
    }

    ByteBuffer  writeBBuf = ByteBuffer.allocate(8192);
	private void write2Channel(byte[] audio) throws IOException {
		writeBBuf = ByteBuffer.wrap(audio);
        while(writeBBuf.hasRemaining()){
            int nBytes = client.write(writeBBuf);
        }

        writeBBuf.rewind();

    }
	
	public void sendBytes(byte[] bytes) throws IOException, InterruptedException{
		
		write2Channel(bytes);
		//closeConnection();
	}

}
