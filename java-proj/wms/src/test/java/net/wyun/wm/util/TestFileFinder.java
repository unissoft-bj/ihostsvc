/**
 * 
 */
package net.wyun.wm.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import net.wyun.wm.audio.FileService;
import net.wyun.wm.util.file.FileFinder;

import org.junit.Test;

/**
 * @author michael
 *
 */
public class TestFileFinder {

	@Test
	public void testFindWavFileOlderThan() throws IOException {
		FileFinder finder = new FileFinder("*.wav", 30);
		Files.walkFileTree(Paths.get("/home/michael/temp"), finder);
		
		Collection<Path> matchedFiles = finder.getMatchedPaths();
		
		for(Path path : matchedFiles){
			System.out.println(path.getParent().toString() + '/' + path.getFileName());
			//path.toFile().delete();
		}
	}

}
