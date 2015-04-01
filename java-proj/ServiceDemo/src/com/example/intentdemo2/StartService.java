package com.example.intentdemo2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class StartService extends Service {
	private final static String TAG = "main";

	@Override
	public IBinder onBind(Intent arg0) {
		// ��ͨ��startService()�����������������������null���ɡ�
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Service is Created");		
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

}
