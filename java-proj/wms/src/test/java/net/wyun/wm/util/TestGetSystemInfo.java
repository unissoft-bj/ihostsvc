package net.wyun.wm.util;

import java.util.concurrent.TimeUnit;

import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import org.junit.Test;

public class TestGetSystemInfo {

	@Test
	public void testCpuLoad() throws MalformedObjectNameException, InstanceNotFoundException, ReflectionException, InterruptedException{
		
		for(int i=0; i < 10; i++){
			
			double cl = SystemInfoUtil.getProcessCpuLoad();
			
			System.out.println("cpu load: " + cl);
			
			TimeUnit.SECONDS.sleep(1);
		}
		
	}
}
