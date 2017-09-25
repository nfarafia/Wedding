package com.vergiliy.wedding.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.text.TextUtils;

import com.vergiliy.wedding.R;

public class SettingsGeneralFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_general);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
            Preference preference = getPreferenceScreen().getPreference(i);
            if (preference instanceof PreferenceGroup) {
                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                    Preference singlePref = preferenceGroup.getPreference(j);
                    updatePreference(singlePref, singlePref.getKey());
                }
            } else {
                updatePreference(preference, preference.getKey());
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreference(findPreference(key), key);
    }

    // Update summary text
    private void updatePreference(Preference preference, String key) {
        CharSequence text = null;

        if (preference == null) return;

        // Summary for ListPreference
        if (preference instanceof ListPreference) {
            ListPreference list = (ListPreference) preference;
            list.setSummary(list.getEntry());
        // Summary for DatePreference
        } else if (preference instanceof DatePreference) {
            SharedPreferences SharedPreferences = getPreferenceManager().getSharedPreferences();
            text = SharedPreferences.getString(key, null);
            if (text != null) {
                DatePreference date = (DatePreference) preference;
                text = date.getDateForSummary(text.toString());
            }
        // Summary for TimePreference
        } else if (preference instanceof TimePreference) {
            SharedPreferences SharedPreferences = getPreferenceManager().getSharedPreferences();
            text = SharedPreferences.getString(key, null);
            if (text != null) {
                TimePreference time = (TimePreference) preference;
                text = time.getTimeForSummary(text.toString());
            }
        // Summary for another elements
        } else {
            SharedPreferences SharedPreferences = getPreferenceManager().getSharedPreferences();
            text = SharedPreferences.getString(key, null);
        }

        // If getting text not found, return default summary
        if (TextUtils.isEmpty(text)) {
            String name = "settings_" + key + "_title";
            Integer id = getResources()
                    .getIdentifier(name, "string", getActivity().getPackageName());
            try {
                text = getResources().getString(id);
            } catch (Exception e) {
                System.out.println("String " + name + " not found; " + e.getMessage());
            }
        }

        preference.setSummary(text);
    }
}