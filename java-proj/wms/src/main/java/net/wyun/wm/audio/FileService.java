package net.wyun.wm.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import net.wyun.wm.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;



/**
 * Manages audio files.
 * <p>
 * Last Modified: $Date$
 * audio files folder /wms/audio/user/2012-02-01/
 * @author Xuecheng
 */
@Service
@EnableScheduling
@Scope("singleton")
public class FileService {
	
	private static final Logger logger = LoggerFactory.getLogger(FileService.class);
	
	public static String audioDir="/wms/audio/";
	
	public static String YMD;
	
	public static Set<String> users;
	
	public FileService() { 
		YMD = this.getYMD(Calendar.getInstance());	
		users = new HashSet<String>();
	}
	
	@Scheduled (cron = "0 0 0 * * *") // everyday at midnight (cron= "0 0/2 * * * ?") //every 2 minutes
    public void updatYMD() throws InterruptedException{
		TimeUnit.SECONDS.sleep(5);
		YMD = this.getYMD(Calendar.getInstance());
        logger.info("update Today YMD number : {}", YMD);
        System.out.println("cron job every day at midnight.");
        
        for(String user:users){
        	checkUserDir(user);    //assume that there is no recording going on
        }
        
    }
	
    
    private void checkDir(File dir) {
    	synchronized(this) { //for now, we only have one thread
			if (!dir.isDirectory()) {
				logger.info("Creating directory " + dir.getPath());
				dir.mkdirs();
			}
		}
    }
    
    public void checkUserDir(String user){
    	users.add(user);
    	
    	File f = new File(audioDir + user);
    	checkDir(f);
    	
    	f = new File(audioDir+user + "/" + YMD);
    	checkDir(f);
    }
    
    
    public File createDestFile(String fileName, String type, String userId) {
    	if (type == null) {
    		return createDestFile(fileName);
    	}
    	
    	File dir = new File(audioDir + userId + "/" + type);
    	checkDir(dir);
    	return new File(dir, fileName);
    }
    
    public File createDestFile(String fileName) {
    	int idx = fileName.lastIndexOf('.');
    	String ext = ((idx >= 0 && idx < fileName.length() - 1) ? fileName.substring(idx) : "");
    	Calendar cal = Calendar.getInstance();
    	File dir = getDestDir(cal);
    	long ts = cal.getTimeInMillis();
    	File f = new File(dir, ts + ext);
    	
    	return f;
    }
    
    public String getYMD(Calendar cal){
    	int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String dirName = String.format("%d-%02d-%02d", year, month, day);
		return dirName;
    }
    
    private File computeDestDir(Calendar cal) {
    	int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String dirName = audioDir + String.format("%d/%02d%02d", year, month, day);
		File dir = new File(dirName);
		return dir;
    }
    
    private File getDestDir(Calendar cal) {
		File dir = computeDestDir(cal);
		checkDir(dir);
		
		return dir;
    }
    
    public FileInputStream getFile(String file, String type) throws FileNotFoundException {
    	if (type == null) {
    		return getFile(file);
    	}
    	
    	return new FileInputStream(new File(audioDir + file));
    }
    
    public FileInputStream getFile(String file) throws FileNotFoundException {
    	// The first part of the file should be the time stamp
    	int idx = file.indexOf('.');
    	String s;
    	if (idx < 0) {
    		s = file;
    	} else {
    		s = file.substring(0, idx);
    	}
    	
    	long ts;
    	try {
    		ts = Long.parseLong(s);
    	} catch (NumberFormatException e) {
    		logger.error("Error parsing the timestamp in file " + file + ": " + e);
    		return null;
    	}
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(ts);
    	File dir = computeDestDir(cal);
    	return new FileInputStream(new File(dir, file));
    }
}
