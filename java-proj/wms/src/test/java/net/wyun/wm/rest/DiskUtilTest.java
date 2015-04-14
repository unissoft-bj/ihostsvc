package net.wyun.wm.rest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import net.wyun.wm.rest.disk.Disk;
import net.wyun.wm.rest.disk.DiskUtil;

import org.junit.Test;

public class DiskUtilTest {

	@Test
	public void test() throws IOException {
		List<Disk> ld = DiskUtil.getDiskSpace();
		for(Disk d:ld){
			System.out.println(d.toString());
		}
	}

}
