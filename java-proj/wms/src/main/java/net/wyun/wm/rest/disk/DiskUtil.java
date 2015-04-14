/**
 * 
 */
package net.wyun.wm.rest.disk;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xuecheng
 *
 */
public class DiskUtil {
	
    public static List<Disk> getDiskSpace() throws IOException{
    	
    	List<Disk> disks = new ArrayList<Disk>();
    	
    	NumberFormat nf = NumberFormat.getNumberInstance();
    	for (Path root : FileSystems.getDefault().getRootDirectories())
    	{
    		String availSpace;
        	String diskSpace;
    	    String r_s = root.toString();

    	    try
    	    {
    	        FileStore store = Files.getFileStore(root);
    	        availSpace = nf.format(store.getUsableSpace());
    	        diskSpace = nf.format(store.getTotalSpace());
    	        System.out.println("available=" + availSpace + ", total=" + diskSpace);
    	        Disk disk = new Disk(r_s, availSpace, diskSpace);
    	        disks.add(disk);
    	    }
    	    catch (FileSystemException e)
    	    {
    	        System.out.println("error querying space: " + e.toString());
    	    }
    	    
    	}
    	
    	return disks;
    }

}
