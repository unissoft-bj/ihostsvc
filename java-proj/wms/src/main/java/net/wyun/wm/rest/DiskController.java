/**
 * 
 */
package net.wyun.wm.rest;

import java.io.IOException;
import java.util.List;

import net.wyun.wm.rest.disk.Disk;
import net.wyun.wm.rest.disk.DiskInfo;
import net.wyun.wm.rest.disk.DiskUtil;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xuecheng
 *
 */
@RequestMapping("/secure")
@RestController
public class DiskController {
	
	@RequestMapping("/disk")
	DiskInfo getDiskUsage() throws IOException{
		//first call util to get disk usage
		List<Disk> ld = DiskUtil.getDiskSpace();
		DiskInfo di = new DiskInfo();
		di.setDiskList(ld);
		return di;
	}
	

}
