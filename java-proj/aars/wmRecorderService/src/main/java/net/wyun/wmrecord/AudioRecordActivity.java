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
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
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
    private Intent service=null;
	private static String[] freqText = {"11.025 KHz"}; //, "16.000 KHz", "22.050 KHz", "44.100 KHz (Highest)"};
	private static Integer[] freqset = {11025}; //, 16000, 22050, 44100};
	private ArrayAdapter<String> adapter;

	Spinner spFrequency;
	Button setting;
    Button pauseRec;

	Boolean recording;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.LOG_TAG, "aar activity created.");
        setContentView(R.layout.main);
        pauseRec = (Button)findViewById(R.id.pauserec);
        setting = (Button)findViewById(R.id.setting);

        pauseRec.setOnClickListener(pauseRecOnClickListener);
        setting.setOnClickListener(playBackOnClickListener);

        spFrequency = (Spinner)findViewById(R.id.frequency);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, freqText);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFrequency.setAdapter(adapter);

        service = new Intent(AudioRecordActivity.this,StartService.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(this.LOG_TAG, "aar activity resumes.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(this.LOG_TAG, "aar activity partially visible."); //it should be still recording
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(this.LOG_TAG, "aar activity is in hidden state. recording stops here");
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

                Log.i(this.LOG_TAG,i.toString());

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
    
    OnClickListener pauseRecOnClickListener
            = new OnClickListener(){
        @Override
        public void onClick(View arg0) {

            startService(service);
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
}