/**
 * 
 */
package net.wyun.wm.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Xuecheng
 *
 */
@Component
public class InternetGuardMock implements InternetGuard{

	private static final Logger logger = LoggerFactory.getLogger(InternetGuardMock.class);
	@Override
	public void authorize(String ip) {
		logger.info("open internet for ip: {}", ip);
	}

	@Override
	public void logoff(String mac) {
		logger.info("close internet for mac: {}", mac);
		
	}

}
