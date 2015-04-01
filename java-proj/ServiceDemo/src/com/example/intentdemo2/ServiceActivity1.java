package com.example.intentdemo2;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ServiceActivity1 extends Activity {
	private Button btnSer1, btnSer2;
	private Intent service=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service1);
		btnSer1 = (Button) findViewById(R.id.btnSer1);
		btnSer2 = (Button) findViewById(R.id.btnSer2);
		// 设置服务启动的Intent
		service=new Intent(ServiceActivity1.this,StartService.class);
		btnSer1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 启动服务
				startService(service);
			}
		});

		btnSer2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 停止服务
				stopService(service);
			}
		});
	}
}
