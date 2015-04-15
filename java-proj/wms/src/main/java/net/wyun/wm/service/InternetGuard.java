/**
 * 
 */
package net.wyun.wm.service;

import org.springframework.stereotype.Service;

/**
 * @author Xuecheng
 *
 */
@Service
public interface InternetGuard {
	
	 //接通internet
	 //chilli_query authorize ip 172.16.0.105 sessiontimeout 86400
	void authorize(String ip);
	
	//关闭internet
	// chilli_query logout 00-73-E0-67-42-E2
	void logoff(String mac);
	

}
