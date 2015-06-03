package net.wyun.wm.util;

import java.util.LinkedList;
import java.util.List;

import net.wyun.wm.util.ProcessExecutor.ProcessExecutorHandler;

public class WmsProcessExecutorHandler implements ProcessExecutorHandler{
	
	public List<String> executionResults = new LinkedList<String>();
	public List<String> errors = new LinkedList<String>();

	@Override
	public void onStandardOutput(String msg) {
		executionResults.add(msg);
	}

	@Override
	public void onStandardError(String msg) {
		errors.add(msg);
	}

}
