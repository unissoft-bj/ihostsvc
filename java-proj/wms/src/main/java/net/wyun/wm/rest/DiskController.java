/**
 * 
 */
package net.wyun.wm.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import net.wyun.wm.audio.FileService;
import net.wyun.wm.rest.disk.Disk;
import net.wyun.wm.rest.disk.DiskInfo;
import net.wyun.wm.rest.disk.DiskUtil;
import net.wyun.wm.util.file.FileFinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xuecheng
 *
 */
@RequestMapping("/secure")
@RestController
public class DiskController {
	
	private static final Logger logger = LoggerFactory.getLogger(DiskController.class);
	
	@RequestMapping(value="/disk", method=RequestMethod.GET)
	DiskInfo getDiskUsage() throws IOException{
		//first call util to get disk usage
		List<Disk> ld = DiskUtil.getDiskSpace();
		DiskInfo di = new DiskInfo();
		di.setDiskList(ld);
		return di;
	}
	

	@RequestMapping(value="/cleandisk/{days}", method=RequestMethod.PUT)
	public void cleanDisk(@PathVariable("days") int days) throws IOException{
		//first call util to get disk usage
		
		FileFinder finder = new FileFinder("*.wav", days);
		Files.walkFileTree(Paths.get(FileService.audioDir), finder);
		
		Collection<Path> matchedFiles = finder.getMatchedPaths();
		
		for(Path path : matchedFiles){
			System.out.println(path.getFileName());
			logger.info("delete file: " + path.getFileName());
			//path.toFile().delete();
		}
		
	}
	
}
