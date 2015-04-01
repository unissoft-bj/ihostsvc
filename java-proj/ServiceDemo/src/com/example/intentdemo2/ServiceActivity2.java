package com.example.intentdemo2;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ServiceActivity2 extends Activity {
	private final String TAG = "main";
	Button bind, unbind, getServiceStatus;
	BindService.MyBinder binder;
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {			
			Log.i(TAG, "--Service Disconnected--");
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(TAG, "--Service Connected--");
			// ȡ��Service�����е�Binder����
			binder = (BindService.MyBinder) service;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bindservice1);
		
		bind = (Button) findViewById(R.id.bind);
		unbind = (Button) findViewById(R.id.unbind);
		getServiceStatus = (Button) findViewById(R.id.getServiceStatus);
		
		final Intent intent = new Intent();
		// ָ�����������action
		intent.setAction("com.bgxt.BindServiceDemo");
		
		bind.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// �󶨷��񵽵�ǰactivity��
				bindService(intent, conn, Service.BIND_AUTO_CREATE);
			}
		});
		unbind.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// �����
				binder=null;
				unbindService(conn);
			}
		});
		getServiceStatus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(binder!=null)
				{
					// ͨ���󶨷��񴫵ݵ�Binder���󣬻�ȡService��¶����������
					Toast.makeText(ServiceActivity2.this,
							"Service��CountֵΪ" + binder.getCount(),
							Toast.LENGTH_SHORT).show();
					Log.i(TAG, "Service��CountֵΪ" + binder.getCount());
				}
				else
				{
					Toast.makeText(ServiceActivity2.this,
							"��û���أ��Ȱ󶨡�",
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}

}
