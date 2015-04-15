package net.wyun.wm.domain.mac;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.wyun.wm.service.ChilliQueryProcessExecutorHandler;
import net.wyun.wm.util.ProcessExecutor;

import org.apache.commons.exec.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("macAddrLookupService")
public class MacAddressLookupServiceImpl implements MacAddressLookupService{

	private static final Logger logger = LoggerFactory.getLogger(MacAddressLookupServiceImpl.class);
	@Override
	public String getMacAddrByIP(String ip) {
		// TODO: map ip to mac address in Chilli
		List<String> lstr = new LinkedList<String>();
		try {
			lstr = getHandler().executionResults;
			logger.info("chilli_query list results: " + lstr);
		} catch (IOException | InterruptedException | ExecutionException
				| TimeoutException e) {
			e.printStackTrace();
		}
		Optional<String> os = lstr.stream().reduce((x, y) -> (x + "\n" + y));
		logger.debug("list: {}", os.get());
		
		//search for ip
		String mac = extractMac(lstr, ip);
		
		return mac;

	}
	
	public static String extractMac(List<String> devices, String ip){
		String device = "";
		for(String s:devices){
			if(s.contains(ip)){
				device = s;
				break;
			}
		}
		if(device.isEmpty()) return null;
		//we have the string contains the ip, extract mac here
		int ind = device.indexOf(ip);
		return device.substring(0, ind).trim();
		
	}
	
	private ChilliQueryProcessExecutorHandler getHandler() throws IOException, InterruptedException, ExecutionException, TimeoutException{
		ChilliQueryProcessExecutorHandler chHandler = new ChilliQueryProcessExecutorHandler();
		Future<Long> future =	ProcessExecutor.runProcess(CommandLine.parse("chilli_query list"), chHandler, 1500);
		Long ret = future.get(1, TimeUnit.SECONDS);
		logger.debug("return code: {}", ret);
		return chHandler;
	}

}
