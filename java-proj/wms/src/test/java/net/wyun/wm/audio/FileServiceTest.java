package net.wyun.wm.audio;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileServiceTest {
	
	FileService fs;
	@Before
    public void setUp() throws IOException {
        System.out.println("startUp()");
        fs = new FileService();
    }
	
	 @After
	 public void tearDown() {
	       System.out.println("FileService test done.");
	 }

	@Test
	public void testUpdatYMD() throws InterruptedException {
		fs.updatYMD();
	}

	@Test
	public void testCheckUserDir() {
		fs.checkUserDir("user-test");
	}

	@Test
	public void testGetYMD() {
        fs.getYMD(Calendar.getInstance());
	}

}
