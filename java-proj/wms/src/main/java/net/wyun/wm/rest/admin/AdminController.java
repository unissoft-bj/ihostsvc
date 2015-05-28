/**
 * 
 */
package net.wyun.wm.rest.admin;


import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.wyun.wm.data.IhostCmdRequest;
import net.wyun.wm.util.ProcessExecutor;
import net.wyun.wm.util.WmsProcessExecutorHandler;

import org.apache.commons.exec.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author michael
 *
 */
@RequestMapping("/admin")
@RestController
public class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	/**
	 * ihostset.sh reboot
	 * ihostset.sh systime local '2015-05-23 21:03:01
	 * @param cmdReq
	 * @return
	 */
	@RequestMapping(value="/ihostset", method=RequestMethod.POST)
	public String configureIhost(@RequestBody IhostCmdRequest cmdReq){
		//reboot ihost
		String cmd = cmdReq.getCommand();
		logger.info("configure ihost now: " + cmd);
		WmsProcessExecutorHandler chHandler = new WmsProcessExecutorHandler();
		Future<Long> future;
		Long ret = 0L;
		try {
			future = ProcessExecutor.runProcess(CommandLine.parse(cmd), chHandler, 3000);
			ret = future.get(3, TimeUnit.SECONDS);
			
		} catch (IOException | InterruptedException | ExecutionException
				| TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String msg = "";
		if(!chHandler.executionResults.isEmpty()){
			msg = chHandler.executionResults.get(0);
		}
		
		String err = "";
		if(!chHandler.errors.isEmpty()){
			err = chHandler.errors.get(0);
		}
		
		String json = "{'ret': '" + ret + "'," + "'msg':'" + msg + "'," + "'err':'" + err + "'";  
		return json; 
		
	}
	
	

}
