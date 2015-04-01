package com.example.intentdemo2;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class StartIntentService1 extends IntentService {
	private final static String TAG = "main";
	public StartIntentService1() {
		super("IntentServiceName");
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "Service is Created");
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "Service is started");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "Service is Destroyed");
		super.onDestroy();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		
		Log.i(TAG, "HandleIntent±»Ö´ÐÐ");

	}
	

}
