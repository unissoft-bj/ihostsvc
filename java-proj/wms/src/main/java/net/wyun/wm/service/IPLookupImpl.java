package net.wyun.wm.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("production")
public class IPLookupImpl implements IPLookup {
	
	public IPLookupImpl() {
		u_map = new ConcurrentHashMap<String, String>();
		m_map = new ConcurrentHashMap<String, String>();
	}

	private static ConcurrentHashMap<String, String> u_map; //ip --> user (or phone number)
	private static ConcurrentHashMap<String, String> m_map; //ip --> mac

	@Override
	public void addUser(String ip, String name, String mac){
		u_map.put(ip, name);
		m_map.put(ip, mac);
	}
	
	@Override
	public String getMAC(String ip) {
		return m_map.get(ip);
	}

	private AtomicInteger num = new AtomicInteger();
	
	@Override
	public String getUserName(String ip) {
		
		return u_map.get(ip);
	}

}
