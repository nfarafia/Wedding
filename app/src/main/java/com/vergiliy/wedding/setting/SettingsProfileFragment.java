package com.vergiliy.wedding.setting;

import android.os.Bundle;

import com.vergiliy.wedding.R;

public class SettingsProfileFragment extends BasePreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_profile);
    }
}