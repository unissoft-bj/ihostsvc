/**
 * 
 */
package net.wyun.wm.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.wyun.wm.service.ChilliQueryProcessExecutorHandler;

import org.apache.commons.exec.CommandLine;
import org.junit.Test;

/**
 * @author Xuecheng
 *
 */
public class ProcessExecutorTest {

	@Test
	public void testWithAnonymousHandler() throws IOException, InterruptedException, ExecutionException {
		
		ProcessExecutor.ProcessExecutorHandler handler = new ProcessExecutor.ProcessExecutorHandler() {
			
			@Override
			public void onStandardOutput(String msg) {
				System.out.println("dir output: \n" + msg);
			}
			
			@Override
			public void onStandardError(String msg) {
				System.out.println("dir error: \n" + msg);
			}
		};
		
		Future<Long> future =	ProcessExecutor.runProcess(CommandLine.parse("ping www.google.com"), handler, 4000);
		System.out.println("git --version command status: \n" + future.get());
	}
	
	@Test
	public void testWithChilliHandler() throws IOException, InterruptedException, ExecutionException, TimeoutException {
	
		ChilliQueryProcessExecutorHandler chHandler = new ChilliQueryProcessExecutorHandler();
		Future<Long> future =	ProcessExecutor.runProcess(CommandLine.parse("git --version"), chHandler, 1000);
		Long ret = future.get(1, TimeUnit.SECONDS);
		System.out.println("git --version command status: \n" + ret + ", \nmessage: \n" + chHandler.executionResults);
	}

}
