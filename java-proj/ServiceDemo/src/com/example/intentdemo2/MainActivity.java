package com.example.intentdemo2;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button btnMain1, btnMain2,btnMain3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnMain1 = (Button) findViewById(R.id.btnMain1);
		btnMain2 = (Button) findViewById(R.id.btnMain2);
		btnMain3 = (Button) findViewById(R.id.btnMain3);
		btnMain1.setOnClickListener(click);
		btnMain2.setOnClickListener(click);
		btnMain3.setOnClickListener(click);
	}

	private View.OnClickListener click = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			Intent intent = null;
			switch (v.getId()) {
			case R.id.btnMain1:
				intent=new Intent(MainActivity.this, ServiceActivity1.class);
				startActivity(intent);
				break;

			case R.id.btnMain2:
				intent=new Intent(MainActivity.this, ServiceActivity2.class);
				startActivity(intent);
				break;
			case R.id.btnMain3:
				intent=new Intent(MainActivity.this, StartIntentActivity1.class);
				startActivity(intent);
				break;
			}

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
