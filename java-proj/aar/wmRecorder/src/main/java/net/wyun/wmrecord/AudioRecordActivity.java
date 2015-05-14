package net.wyun.wmrecord;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class AudioRecordActivity extends Activity {


    private final String LOG_TAG = AudioRecordActivity.class.getSimpleName();
	
	private static String[] freqText = {"11.025 KHz"}; //, "16.000 KHz", "22.050 KHz", "44.100 KHz (Highest)"};
	private static Integer[] freqset = {11025}; //, 16000, 22050, 44100};
	private ArrayAdapter<String> adapter;
	
	Spinner spFrequency;
	Button startRec, stopRec, setting;
	
	Boolean recording;

    public static DatagramSocket socket;
    private String serverAddr = "192.168.1.7";
    private int port = 8888;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.LOG_TAG, "aar activity created.");
        setContentView(R.layout.main);
        startRec = (Button)findViewById(R.id.startrec);
        stopRec = (Button)findViewById(R.id.stoprec);
        setting = (Button)findViewById(R.id.setting);
        
        startRec.setOnClickListener(startRecOnClickListener);
        stopRec.setOnClickListener(stopRecOnClickListener);
        setting.setOnClickListener(playBackOnClickListener);
        
        spFrequency = (Spinner)findViewById(R.id.frequency);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, freqText);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFrequency.setAdapter(adapter);

        stopRec.setEnabled(false);
        updateServerIP();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(this.LOG_TAG, "aar activity resumes.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(this.LOG_TAG, "aar activity partially visible."); //it should be still recording
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(this.LOG_TAG, "aar activity is in hidden state. recording stops here");
    }

    private void updateServerIP(){
        //get ihost ip if available
        String ip = this.getSetting(KEY_IP);
        serverAddr = ip.isEmpty()?serverAddr:ip;
        Log.d(this.LOG_TAG, "audio server: " + serverAddr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    private static final int RESULT_SETTINGS = 1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_settings:
                Intent i = new Intent(this, UserSettingActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                showUserSettings();
                break;
        }

    }

    private static String KEY_IP = "prefIhostIp";
    private String getSetting(String key){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String value = sharedPrefs.getString(KEY_IP, "");
        return value;
    }
    private void showUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();

        builder.append("\n Username: "
                + sharedPrefs.getString("prefUsername", "NULL"));

        builder.append("\n Send report:"
                + sharedPrefs.getBoolean("prefSendReport", false));

        builder.append("\n Sync Frequency: "
                + sharedPrefs.getString("prefSyncFrequency", "NULL"));

    }
    
    OnClickListener startRecOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			
			Thread recordThread = new Thread(new Runnable(){

				@Override
				public void run() {
					recording = true;
                    try {
                        startRecord();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
				
			});
			
			recordThread.start();
			startRec.setEnabled(false);
			stopRec.setEnabled(true);

		}};
		
	OnClickListener stopRecOnClickListener
	= new OnClickListener(){
		
		@Override
		public void onClick(View arg0) {
			recording = false;
			startRec.setEnabled(true);
			stopRec.setEnabled(false);
		}};
		
	OnClickListener playBackOnClickListener
	    = new OnClickListener(){

			@Override
			public void onClick(View v) {
                startSettingActivity();
			}
		
	};

    private void startSettingActivity(){
        Intent i = new Intent(this, UserSettingActivity.class);
        startActivityForResult(i, RESULT_SETTINGS);
    }

    private SocketChannel client;
    private void connectAudioServer() throws IOException, InterruptedException {

        client = SocketChannel.open();
        client.configureBlocking(false);
        InetSocketAddress isa = new InetSocketAddress(serverAddr, 8001);
        client.connect(isa);

        while(! client.finishConnect() ){
            TimeUnit.SECONDS.sleep(1);
        }  //wait, or do something else...
        Log.i(this.LOG_TAG, "connected to server.");
    }

    private void closeConnection() throws IOException {
        client.close();
    }

    ByteBuffer  writeBBuf = ByteBuffer.allocate(8192);
	private void startRecord() throws UnknownHostException, InterruptedException {
        this.updateServerIP();
        final InetAddress destination = InetAddress.getByName(serverAddr);
        Log.d(this.LOG_TAG, "Address retrieved: " + serverAddr);
		
		int selectedFreqInd = spFrequency.getSelectedItemPosition();
		int sampleFreq = freqset[selectedFreqInd];

        AudioRecord audioRecord = null;
		try {

            connectAudioServer();

			int minBufferSize = AudioRecord.getMinBufferSize(sampleFreq, 
					AudioFormat.CHANNEL_CONFIGURATION_MONO, 
					AudioFormat.ENCODING_PCM_16BIT);

            Log.d(this.LOG_TAG, "min buffer size for audio recording: " + minBufferSize);
            if(minBufferSize < 8192) minBufferSize = 8192;  //to get better recording, 8192
			byte[] audioData = new byte[minBufferSize];
			
			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
					sampleFreq,
					AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT,
					minBufferSize);
			
			audioRecord.startRecording();
            socket = new DatagramSocket();

			while(recording){
				int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);

                //putting buffer in the packet
              // DatagramPacket packet = new DatagramPacket(audioData, audioData.length, destination, port);

               write2Channel(audioData);

			}
			socket = null;
			audioRecord.stop();
  		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            //release resource
            Log.d(this.LOG_TAG, "remember release resource here");
           if(audioRecord != null) audioRecord.release();
           if(client != null) try {
               closeConnection();
           } catch (IOException e) { }

        }

	}

    private void write2Channel(byte[] audio) throws IOException {
        writeBBuf= ByteBuffer.wrap(audio);
        while(writeBBuf.hasRemaining()){
            int nBytes = client.write(writeBBuf);
            Log.d(this.LOG_TAG, "send pkt to server, bytes: " + nBytes);
        }

        writeBBuf.rewind();

    }

	
}