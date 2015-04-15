/**
 * 
 */
package net.wyun.wm.domain;

import static org.junit.Assert.*;
import net.wyun.wm.domain.mac.MacAddressUtil;

import org.junit.Test;

/**
 * @author Xuecheng
 *
 */
public class MacAddrTest {
	//0123456789AB

	@Test
	public void testLong_to_MacAddr() {

		Long mac_L = 1250999896491L;
		
		String hex = Long.toString(mac_L, 16);
		
		System.out.println(hex);
		
		hex = MacAddressUtil.toMacString(mac_L);
		
		assertEquals(hex.toUpperCase(), "0123456789AB");
		
	}
	
	//00-FF-32-CC-E9-FB
	@Test
	public void testMacAddr_to_Long() {

		String mac_s = "00-FF-32-CC-E9-FB";
		
		Long mac_l_10 = MacAddressUtil.toLong(mac_s);
		
		System.out.println(mac_l_10);
		System.out.println("hex str: " + Long.toHexString(mac_l_10));
		
		String hex = MacAddressUtil.toMacString(mac_l_10);
		
		assertEquals(hex.toUpperCase(), "00FF32CCE9FB");
		
	}
	
	@Test
	public void testRandomMacAddrGenerator(){
		String ranMac = MacAddressUtil.randomMacAddr();
		System.out.println("Mac address: " + ranMac);
	}
	

}
