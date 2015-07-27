package net.wyun.wmrecord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by guof on 7/25/2015.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context,Intent
            intent){

        Intent service =new Intent(context,MessengerService.class);
        context.startService(service);

        Log.i("this", "start service ");
    }
}
