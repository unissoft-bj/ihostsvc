package net.wyun.wmrecord;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    float br = (float) 0.01;

    private static String[] freqText = {"11.025 KHz"}; //, "16.000 KHz", "22.050 KHz", "44.100 KHz (Highest)"};
    private static Integer[] freqset = {11025}; //, 16000, 22050, 44100};
    private ArrayAdapter<String> adapter;

    Spinner spFrequency;
    Button startRec, stopRec, setting;

    Boolean recording = false;

    public static DatagramSocket socket;
    private String serverAddr = "192.168.1.7";
    private int port = 8888;

    Vibrator vibrator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //方法一
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        UtilHelper.brightnessPreview(this,br);

        Log.d(this.LOG_TAG, "aar activity created.");

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        startRec = (Button)findViewById(R.id.btnStartService);
        stopRec = (Button)findViewById(R.id.btnStopService);

        startRec.setOnClickListener(startRecOnClickListener);
        stopRec.setOnClickListener(stopRecOnClickListener);
        //setting.setOnClickListener(playBackOnClickListener);

        spFrequency = (Spinner)findViewById(R.id.frequency);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, freqText);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFrequency.setAdapter(adapter);
        //spFrequency.setFocusable(false);

        stopRec.setEnabled(false);
        updateServerIP();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private static final int RESULT_SETTINGS = 1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, UserSettingActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    View.OnClickListener startRecOnClickListener = new View.OnClickListener(){

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
                    }
                }

            });

            recordThread.start();
            startRec.setEnabled(false);
            stopRec.setEnabled(true);

        }};

    View.OnClickListener stopRecOnClickListener
            = new View.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            recording = false;
            startRec.setEnabled(true);
            stopRec.setEnabled(false);
        }};

    View.OnClickListener playBackOnClickListener
            = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            startSettingActivity();
        }

    };

    private void startSettingActivity(){
        Intent i = new Intent(this, UserSettingActivity.class);
        startActivityForResult(i, RESULT_SETTINGS);
    }

    private void updateServerIP(){
        //get ihost ip if available
        String ip = this.getSetting(KEY_IP);
        serverAddr = ip.isEmpty()?serverAddr:ip;
        Log.d(this.LOG_TAG, "audio server: " + serverAddr);
    }

    private void startRecord() throws UnknownHostException {

        int pktCount = 0;
        this.updateServerIP();
        final InetAddress destination = InetAddress.getByName(serverAddr);
        Log.d(this.LOG_TAG, "Address retrieved: " + serverAddr);

        int selectedFreqInd = spFrequency.getSelectedItemPosition();
        int sampleFreq = freqset[selectedFreqInd];

        AudioRecord audioRecord = null;
        try {

            int minBufferSize = AudioRecord.getMinBufferSize(sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            Log.d(this.LOG_TAG, "min buffer size for audio recording: " + minBufferSize);
            if(minBufferSize < 8192) minBufferSize = 8192;  //to get better recording, 8192

            //to work with chilli, use smaller udp packet
            minBufferSize = 8960;  //1280 * 7

            byte[] audioData = new byte[minBufferSize];

            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize);

            audioRecord.startRecording();
            socket = new DatagramSocket();

            vibrator.vibrate(AppConstant.ServiceTag.VIBRATOR_PATTERN_START, -1);

            while(recording){
                int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);

                //putting buffer in the packet

                for(int bInd = 0; bInd < 7; bInd++){
                    byte[] slice = Arrays.copyOfRange(audioData, bInd*1280, (bInd + 1)*1280);
                    DatagramPacket packet = new DatagramPacket(slice, slice.length, destination, port);
                    socket.send(packet);
                    Log.d(this.LOG_TAG, "send pkt to server.");
                }
                pktCount +=1;
                if(pktCount == 15000){
                    long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启
                    vibrator.vibrate(AppConstant.ServiceTag.VIBRATOR_PATTERN_REMIND,-1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
                    pktCount = 0;
                }

            }
            socket = null;
            audioRecord.stop();

        } catch (IOException e) {
            e.printStackTrace();
            String msg = e.getMessage();
            Log.i(this.LOG_TAG,msg);
        } finally {
            //release resource
            Log.d(this.LOG_TAG, "remember release resource here");
            if(audioRecord != null) audioRecord.release();

            vibrator.vibrate(AppConstant.ServiceTag.VIBRATOR_PATTERN_STOP, -1);
            recording = false;
            //startRec.setEnabled(true);
            //stopRec.setEnabled(false);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(this.LOG_TAG,"key code: "+String.valueOf(keyCode));
        switch (keyCode) {

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Log.i(this.LOG_TAG, "KEYCODE_VOLUME_DOWN");

                return true;

            case KeyEvent.KEYCODE_MENU:
                //onOptionsItemSelected()

                return true;

            case KeyEvent.KEYCODE_VOLUME_UP:
                Log.i(this.LOG_TAG, "KEYCODE_VOLUME_UP");
                Context mcon = getApplicationContext();

                if(!recording){
                    Thread recordThread = new Thread(new Runnable(){

                        @Override
                        public void run() {
                            recording = true;
                            try {
                                startRecord();
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            }
                        }

                    });

                    recordThread.start();
                    startRec.setEnabled(false);
                    stopRec.setEnabled(true);
                }
                else
                {
                    vibrator.vibrate(AppConstant.ServiceTag.VIBRATOR_PATTERN_REMIND,-1);
                }

                return true;
            case KeyEvent.KEYCODE_VOLUME_MUTE:


                return true;
            case KeyEvent.KEYCODE_ENTER:
                Log.i(this.LOG_TAG,"Enter");
                return false;
        }
        //return super.onKeyDown(keyCode, event);
        return false;
    }
}
