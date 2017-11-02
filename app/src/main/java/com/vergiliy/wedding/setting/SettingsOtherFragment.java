package com.vergiliy.wedding.setting;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.vergiliy.wedding.R;

public class SettingsOtherFragment extends BasePreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_other);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("language")) {

            SettingFragmentsActivity activity = (SettingFragmentsActivity) getActivity();

            // Check is activity available
            if (activity != null){
                activity.getLanguageClass().setLocale();

                // Show back button in ActionBar
                if (activity.getSupportActionBar() != null) {
                    String[] list = getResources().getStringArray(R.array.settings_list_entries);
                    activity.getSupportActionBar().setTitle(list[1]);
                }

                // Rebuild current fragment
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_content, new SettingsOtherFragment())
                        .commit();
            }
        }

        updatePreference(findPreference(key), key);
    }
}