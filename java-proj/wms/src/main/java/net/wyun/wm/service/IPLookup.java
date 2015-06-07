/**
 * 
 */
package net.wyun.wm.service;

import org.springframework.stereotype.Service;

/**
 * @author Xuecheng
 * service do ip lookup to get the mac of the device
 */
@Service
public interface IPLookup {
	
	String getMAC(String ip);
	String getUserName(String ip);
	
	void addUser(String ip, String name, String mac);

}
