package net.wyun.wm.audio.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

public class AudioUtil {
	
	private static int sampleRate = 11025; //44100;
	private static int channelNum = 1;
	public static AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
	public static int FRAME_UNIT_IN_BYTES = channelNum * 2;
	
	
	
	public static void toSpeaker(byte soundbytes[]) {
		try {

			DataLine.Info dataLineInfo = new DataLine.Info(	SourceDataLine.class, format);
			SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

			sourceDataLine.open(format);
			FloatControl volumeControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
			volumeControl.setValue(6.0f); //why 6.0f

			sourceDataLine.open(format);
			sourceDataLine.start();

			System.out.println("format? :" + sourceDataLine.getFormat());

			sourceDataLine.write(soundbytes, 0, soundbytes.length);
			System.out.println(soundbytes.toString());
			sourceDataLine.drain();
			sourceDataLine.close();
		} catch (Exception e) {
			System.out.println("Not working in speakers...");
			e.printStackTrace();
		}
	}
	
	public static void toWAVFile(String fileName, byte[] audio)throws IOException{
	    	File fileOut = new File(fileName);
			OutputStream outstream = new FileOutputStream(fileOut);
			ByteArrayInputStream baiss = new ByteArrayInputStream(audio);
			
			//audio sampling 
			int frameLength = (audio.length)/FRAME_UNIT_IN_BYTES; //how many sampling points in the audioBytes
			AudioInputStream ais = new AudioInputStream(baiss, format, frameLength); //length?
			AudioSystem.write(ais,  AudioFileFormat.Type.WAVE, outstream);
			
			//release
			outstream.close();
			ais.close();
			
		
	}

}
