package com.example.intentdemo2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class StartIntentActivity1 extends Activity {
	private final String TAG="main";
	private Button btnSer1, btnSer2;
	private Intent service=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service1);
		btnSer1 = (Button) findViewById(R.id.btnSer1);
		btnSer2 = (Button) findViewById(R.id.btnSer2);
		btnSer2.setVisibility(0);
		
		service=new Intent(StartIntentActivity1.this,StartIntentService1.class	);
		
		btnSer1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TAG, "Click StartService");
				startService(service);
			}
		});
		
		
	}
}
