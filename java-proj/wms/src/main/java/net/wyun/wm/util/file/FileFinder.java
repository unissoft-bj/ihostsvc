/**
 * 
 */
package net.wyun.wm.util.file;

/**
 * @author michael
 *
 */

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
 
public class FileFinder extends SimpleFileVisitor<Path> {
 
	private final PathMatcher matcher;
	private List<Path> matchedPaths = new ArrayList<Path>();
	
	private int age;  //in days. it means files being created older than age days 
	private long ageInMills;
	private long current;
	
	public FileFinder(String pattern, int age) {
		matcher = FileSystems.getDefault()
				.getPathMatcher("glob:" + pattern);
		this.age = age;
		this.ageInMills = age * 24 * 60 * 60 * 1000L;
		System.out.println("age in mili sec: " + ageInMills);
		this.current = System.currentTimeMillis();
	}
 
	// Compares the glob pattern against
	// the file or directory name.
	void match(Path file) {
		Path name = file.getFileName();
 
		if (name != null && matcher.matches(name)) {
			matchedPaths.add(file);
		}
	}
 
	// Invoke the pattern matching
	// method on each file.
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		FileTime ft = attrs.creationTime();
		long itsAge = current - ft.toMillis();
		
		if(itsAge > ageInMills){
			match(file);
		}
		
		return CONTINUE;
	}
 
	// Invoke the pattern matching
	// method on each directory.
	@Override
	public FileVisitResult preVisitDirectory(Path dir,
			BasicFileAttributes attrs) {
		match(dir);
		return CONTINUE;
	}
 
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		System.err.println(exc);
		return CONTINUE;
	}
	
	public int getTotalMatches() {
		return matchedPaths.size();
	}
 
	public Collection<Path> getMatchedPaths() {
		return matchedPaths;
	}
 
} // class FileFinder ends