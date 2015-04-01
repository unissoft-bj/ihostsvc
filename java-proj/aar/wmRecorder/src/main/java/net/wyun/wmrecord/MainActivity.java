package net.wyun.wmrecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Created by guof on 3/31/2015.
 */
public class MainActivity extends Activity {

    private Button startBtn;
    private Button stopBtn;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startBtn = (Button) findViewById(R.id.startBtn);
        stopBtn = (Button) findViewById(R.id.stopBtn);
        //添加监听器
        startBtn.setOnClickListener(listener);
        stopBtn.setOnClickListener(listener);
    }

    //启动监听器
    private OnClickListener listener=new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent intent=new Intent(MainActivity.this, ServiceDemo.class);
            switch (v.getId())
            {
                case R.id.startBtn:
                    startService(intent);
                    break;
                case R.id.stopBtn:
                    stopService(intent);
                    break;
                default:
                    break;
            }
        }
    };
}
