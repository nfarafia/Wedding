package com.vergiliy.wedding.setting;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.vergiliy.wedding.R;

public class SettingsFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_general);
    }
}
