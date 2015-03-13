/**
 * 
 */
package net.wyun.wm.audio.utils;

/**
 * @author Xuecheng
 * It is a class for testing.
 * We can send generated audio the audio service to testing.
 *
 */
public class AudioGenerator {
	
	private static double sampleRate = 11025.0; // 44100.0;
	//private double frequency = 100;
	private static double frequency2 = 90;
	private static double amplitude = 1.0;
	//double seconds = 360.0;
	
	public static byte[] generateAudio(int seconds, double frequency){
		
		double twoPiF = 2 * Math.PI * frequency;
		double piF = Math.PI * frequency2;
		
		float[] buffer = new float[(int) (seconds * sampleRate)];
		for (int sample = 0; sample < buffer.length; sample++) {
			double time = sample / sampleRate;
			buffer[sample] = (float) (amplitude * Math.cos((double) piF * time) * Math
					.sin(twoPiF * time));
		}
		
		final byte[] byteBuffer = new byte[buffer.length * 2];
		int bufferIndex = 0;
		for (int i = 0; i < byteBuffer.length; i++) {
			final int x = (int) (buffer[bufferIndex++] * 32767.0);
			byteBuffer[i] = (byte) x;
			i++;
			byteBuffer[i] = (byte) (x >>> 8);
		}
		
		return byteBuffer;
	}
	

}
