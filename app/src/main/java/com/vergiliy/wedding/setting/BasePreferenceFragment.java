package com.vergiliy.wedding.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.vergiliy.wedding.BaseClass;
import com.vergiliy.wedding.BaseLocale;
import com.vergiliy.wedding.helpers.BaseHelper;

public class BasePreferenceFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
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
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        if (key.equals("budget")) {
            double budget = BaseHelper.parseDouble(preferences.getString(key, "0"), 0);
            if (budget == 0) {
                preferences.edit().remove(key).apply();
            } else {
                preferences.edit().putString(key, BaseClass.getDoubleAsString(budget)).apply();
            }
        }

        updatePreference(findPreference(key), key);
    }

    // Update summary text
    protected void updatePreference(Preference preference, String key) {
        CharSequence text;

        if (preference == null) return;

        // Summary for ListPreference
        if (preference instanceof ListPreference) {
            ListPreference list = (ListPreference) preference;
            text = list.getEntry();
        // Summary for DatePreference
        } else if (preference instanceof DatePreference) {
            text = sharedPreferences.getString(key, null);
            if (text != null) {
                DatePreference date = (DatePreference) preference;
                text = date.getStringForSummary(text.toString());
            }
        // Summary for TimePreference
        } else if (preference instanceof TimePreference) {
            text = sharedPreferences.getString(key, null);
            if (text != null) {
                TimePreference time = (TimePreference) preference;
                text = time.getTimeForSummary(text.toString());
            }
        // Summary for another elements
        } else {
            text = sharedPreferences.getString(key, null);
        }

        // If getting text not found, return default summary
        if (TextUtils.isEmpty(text)) {
            switch (key) {
                case "language_temp":
                    break;
                case "currency":
                    text = BaseLocale.getLocalCurrency().getCurrencyCode();
                    break;
                default:
                    if (isAdded()) {
                        String name = "settings_" + key + "_summary";
                        Integer id = getResources()
                                .getIdentifier(name, "string", getActivity().getPackageName());
                        try {
                            text = getResources().getString(id);
                        } catch (Exception e) {
                            Log.e("BasePreferenceFragment", "String \"" + name + "\"  not found; " + e.getMessage());

                        }
                    }
            }
        }

        preference.setSummary(text);
    }
}
