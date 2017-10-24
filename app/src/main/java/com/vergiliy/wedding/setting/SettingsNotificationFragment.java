package com.vergiliy.wedding.setting;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.vergiliy.wedding.R;

public class SettingsNotificationFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_notification);
    }
}