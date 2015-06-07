/**
 * 
 */
package net.wyun.wm.service;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author Xuecheng
 *
 */
@Component
@Profile("dev")
public class IPLookupMock implements IPLookup {
	
	public IPLookupMock() {
		u_map = new HashMap<String, String>();
		m_map = new HashMap<String, String>();
		
		
		addUser("192.168.1.2", "5016548878", "22-88-88-FF-FF-22");
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
	@Override
	public void addUser(String ip, String name, String mac) {
		u_map.put(ip, name);
		m_map.put(ip, mac);
	}

}
