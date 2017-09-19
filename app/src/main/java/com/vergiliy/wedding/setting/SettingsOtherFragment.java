package com.vergiliy.wedding.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.vergiliy.wedding.R;

public class SettingsOtherFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_other);
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("language")) {

            SettingFragmentsActivity activity = (SettingFragmentsActivity) getActivity();

            // Check is activity available
            if (activity != null){
                activity.languageClass.setLocale();

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
            // ShortcutIcon();
        }
    }

    /*
    private void ShortcutIcon(){
        Intent shortcutIntent = new Intent(activity, SplashActivity.class);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Test");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(activity, R.mipmap.ic_launcher));
        addIntent.putExtra("duplicate", false);
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        activity.sendBroadcast(addIntent);
    }
    */
}