/**
 * 
 */
package net.wyun.wm.service;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

/**
 * @author Xuecheng
 *
 */
@Component
public class IPLookupMock implements IPLookup {
	
	public IPLookupMock() {
		u_map = new HashMap<String, String>();
		u_map.put("192.168.1.7", "lenovo");
		
		m_map = new HashMap<String, String>();
		m_map.put("192.168.1.7", "lenovo-mac");
	}

	private static HashMap<String, String> u_map; //ip --> user
	private static HashMap<String, String> m_map; //ip --> mac

	
	@Override
	public String getMAC(String ip) {
		return m_map.get(ip);
	}

	private AtomicInteger num = new AtomicInteger();
	@Override
	public String getUserName(String ip) {
		String u = u_map.get(ip);
		if(null == u){
			int cnt = num.incrementAndGet();
			u = "user" + cnt;
			u_map.put(ip, u);
		}
		return u;
	}

}
