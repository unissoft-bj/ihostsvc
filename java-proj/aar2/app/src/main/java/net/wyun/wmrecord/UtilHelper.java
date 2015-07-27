package net.wyun.wmrecord;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by guof on 4/26/2015.
 */
public class UtilHelper {
    final private String LOG_TAG = "MATRIXWS";
    public static boolean isServiceRunning(Context mContext,String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);
        if (!(serviceList.size()>0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            Log.i("MATRIXWS", serviceList.get(i).service.getClassName());
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static String getSetting(Context context,String key) {
        try{
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            String value = sharedPrefs.getString(key, "");
            return value;}
        catch (Exception ex){
            return "";
        }
    }

    public static void setSetting(Context context,String key,String value){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context,String key) {
        try{
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean value = sharedPrefs.getBoolean(key,false);
            return value;}
        catch (Exception ex) {
            return false;
        }
    }

    public static void setBoolean(Context context,String key,boolean value)
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(key,value);
    }

    public static void keepUnLock(){
        //
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    float br = (float) 0.01;

    public static void brightnessPreview(Activity activity, float brightness)
    {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness;
        window.setAttributes(lp);
    }


}
