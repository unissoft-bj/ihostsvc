package net.wyun.wmrecord;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by guof on 3/31/2015.
 */
public class StartService extends Service {
    private final String LOG_TAG = StartService.class.getSimpleName();
    private final static String TAG = "main1";

    Boolean recording;

    public static DatagramSocket socket;
    private String serverAddr = "192.168.199.208";
    private int port = 8888;
    Spinner spFrequency;
    private static String[] freqText = {"11.025 KHz"}; //, "16.000 KHz", "22.050 KHz", "44.100 KHz (Highest)"};
    private static Integer[] freqset = {11025}; //, 16000, 22050, 44100};
    private ArrayAdapter<String> adapter;

    @Override
    public IBinder onBind(Intent arg0) {
        // 仅通过startService()启动服务，所以这个方法返回null即可。
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service is Created");
        //Record();
        updateServerIP();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service is started");
        this.Record();
        return super.onStartCommand(intent, flags, startId);
    }

    private void Record() {
      new Thread(new Runnable() {
          @Override
          public void run() {
              recording = true;
              Log.i("fsdfsdfsdf","sdfosdf");
              try {
                  startRecord();
              } catch (UnknownHostException e) {
                  e.printStackTrace();
              }
          }
      }).start();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service is Destroyed");
        super.onDestroy();
    }

    private static String KEY_IP = "prefIhostIp";

    private String getSetting(String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String value = sharedPrefs.getString(KEY_IP, "");

        Log.i(this.LOG_TAG,value);
        return value;
    }

    private void updateServerIP() {
        //get ihost ip if available
        String ip = this.getSetting(KEY_IP);
        //String ip = "192.168.199.208";
        Log.i(this.LOG_TAG,"ip: "+ip);
        serverAddr = ip.isEmpty() ? serverAddr : ip;
        Log.i(this.LOG_TAG, "audio server: " + serverAddr);
    }

    private void startRecord() throws UnknownHostException {
        this.updateServerIP();
        final InetAddress destination = InetAddress.getByName(serverAddr);
        Log.i(this.LOG_TAG, "Address retrieved: " + serverAddr);

        //int selectedFreqInd = spFrequency.getSelectedItemPosition();
        //int sampleFreq = freqset[selectedFreqInd];

        int sampleFreq = 11025;
        AudioRecord audioRecord = null;
        try {

            int minBufferSize = AudioRecord.getMinBufferSize(sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            Log.i(this.LOG_TAG, "min buffer size for audio recording: " + minBufferSize);
            if (minBufferSize < 8192) minBufferSize = 8192;  //to get better recording, 8192
            byte[] audioData = new byte[minBufferSize];

            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize);

            audioRecord.startRecording();
            socket = new DatagramSocket();

            while (recording) {
                int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);

                //putting buffer in the packet
                DatagramPacket packet = new DatagramPacket(audioData, audioData.length, destination, port);

                socket.send(packet);
                Log.i(this.LOG_TAG, "send pkt to server.");

            }
            socket = null;
            audioRecord.stop();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //release resource
            Log.i(this.LOG_TAG, "remember release resource here");
            if (audioRecord != null) audioRecord.release();
        }

    }
}