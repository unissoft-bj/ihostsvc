package net.wyun.wmrecord;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Xuecheng on 3/16/2015.
 */
public class UserSettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);

    }
}