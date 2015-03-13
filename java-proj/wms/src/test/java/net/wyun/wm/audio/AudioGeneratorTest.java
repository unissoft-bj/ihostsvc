package net.wyun.wm.audio;

import static org.junit.Assert.*;

import java.io.IOException;

import net.wyun.wm.audio.utils.AudioGenerator;
import net.wyun.wm.audio.utils.AudioUtil;

import org.junit.Test;

public class AudioGeneratorTest {

	@Test
	public void GenerateAudioTest() throws IOException {
		
		String fileName = "450hz.wav";
		byte[] audio = AudioGenerator.generateAudio(20, 450); //20 seconds, 450HZ
		AudioUtil.toWAVFile(fileName, audio);
		
	}

}
