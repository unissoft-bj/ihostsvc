/**
 * 
 */
package net.wyun.wm.domain.mac;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author Xuecheng
 *
 */
public class MacAddressLookupServiceImplTest {

	@Test
	public void extractMacTest() {
		List<String> ls = new ArrayList<String>();
		ls.add("80-CF-41-F4-B3-58 172.16.0.102 dnat 552dd4f800000001 0 - 0/0 0/0 0/0 0/0 0 0 0/0 0/0 http://dns.weixin.qq.com/cgi-bin/micromsg-bin/newgetdns?uin=2797953102&clientversion=620757506&scene=0&net=1");
		ls.add("BC-CF-CC-0B-96-48 172.16.0.101 dnat 552db9cf00000002 0 - 0/0 0/0 0/0 0/0 0 0 0/0 0/0 http://xtra3.gpsonextra.net/xtra.bin");
		
		String ip = "172.16.0.102";
		String mac = MacAddressLookupServiceImpl.extractMac(ls, ip);
		assertEquals(mac, "80-CF-41-F4-B3-58");
		
		ip = "172.16.0.101";
		mac = MacAddressLookupServiceImpl.extractMac(ls, ip);
		assertEquals(mac, "BC-CF-CC-0B-96-48");
		
	}

}
