package net.wyun.wmrecord;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by guof on 4/25/2015.
 */
public class UserSettingActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);

    }}
