/**
 * 
 */
package net.wyun.wm.audio;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Xuecheng
 *
 */
public class AudioServiceTest {
	
	AudioServer audioServer;
	
	@Before
    public void setUp() throws IOException {
        System.out.println("startUp()");
    }
	
	 @After
	 public void tearDown() {
	       audioServer.stopSvc();
	       System.out.println("audio server test done.");
	 }

	@Test
	public void startAudioService() throws IOException, InterruptedException {

		TimeUnit.SECONDS.sleep(2);
		System.out.println("test done!");
	}

}
