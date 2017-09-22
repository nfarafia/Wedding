package com.vergiliy.wedding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.vergiliy.wedding.main.SplashActivity;

public class ShortcutIcon {

    private Context context;

    public ShortcutIcon(Context с) {
        context = с;
    }

    // Checked add icon
    private Boolean isAdd(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("icon", false);
    }

    // Create new icon
    public void createShortcutIcon(){
        if (isAdd())
            return;

        Intent shortcutIntent = new Intent(context, SplashActivity.class);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                context.getResources().getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher));
        addIntent.putExtra("duplicate", false);
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);

        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("icon", true).apply();
    }
}
