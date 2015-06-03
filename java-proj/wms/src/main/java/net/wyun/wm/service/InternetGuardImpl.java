/**
 * 
 */
package net.wyun.wm.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.wyun.wm.util.ProcessExecutor;

import org.apache.commons.exec.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Xuecheng
 *
 */
@Component("internetGuardImpl")
public class InternetGuardImpl implements InternetGuard{

	private static final Logger logger = LoggerFactory.getLogger(InternetGuardImpl.class);
	@Override
	public void authorize(String ip) {
		logger.info("open internet for ip: {}", ip);
		try {
			openInternet(ip);
		} catch (IOException | InterruptedException | ExecutionException
				| TimeoutException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void logoff(String mac) {
		logger.info("close internet for mac: {}", mac);
		try {
			closeInternet(mac);
		} catch (IOException | InterruptedException | ExecutionException
				| TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	private final static String open_prefix = "chilli_query authorize ip ";
	private final static String open_affix = " sessiontimeout 86400"; //in configuration file
	private static ChilliQueryProcessExecutorHandler openInternet(String ip) throws IOException, InterruptedException, ExecutionException, TimeoutException{
		ChilliQueryProcessExecutorHandler chHandler = new ChilliQueryProcessExecutorHandler();
		String command = open_prefix + ip; // +  open_affix;
		Future<Long> future =	ProcessExecutor.runProcess(CommandLine.parse(command), chHandler, 1500);
		Long ret = future.get(1, TimeUnit.SECONDS);
		logger.debug("return code: {}", ret);
		return chHandler;
	}
	
	private final static String close_prefix = "chilli_query logout ";
	private ChilliQueryProcessExecutorHandler closeInternet(String mac) throws IOException, InterruptedException, ExecutionException, TimeoutException{
		ChilliQueryProcessExecutorHandler chHandler = new ChilliQueryProcessExecutorHandler();
		String command = close_prefix + mac;
		Future<Long> future =	ProcessExecutor.runProcess(CommandLine.parse(command), chHandler, 1500);
		Long ret = future.get(1, TimeUnit.SECONDS);
		logger.debug("return code: {}", ret);
		return chHandler;
	}
}
