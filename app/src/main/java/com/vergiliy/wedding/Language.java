package com.vergiliy.wedding;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

import static android.content.res.Resources.getSystem;

public class Language {

    protected Context context;

    Language(Context c) {
        context = c;
    }

    // Get current language
    String getLanguage() {
        String language;
        Configuration configuration = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            language = configuration.getLocales().get(0).getLanguage();
        } else {
            language = configuration.locale.getLanguage();
        }
        return language;
    }

    // Get system language
    public Boolean isChangeLanguage(String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferencesLanguage = preferences.getString("language_temp", null);

        // Log.d("Logs", "Language: current = " + language + ", new = " + preferencesLanguage);

        return !(preferencesLanguage == null || language.equals(preferencesLanguage));
    }

    // Set chosen locale
    // @SuppressWarnings("deprecation")
    public void setLocale() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferencesLanguage = preferences.getString("language", "default");

        // Set default language (equal system language)
        if (preferencesLanguage.equals("default")) {
            String systemLanguage;
            Configuration systemConfiguration = getSystem().getConfiguration();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                systemLanguage = systemConfiguration.getLocales().get(0).getLanguage();
            } else {
                systemLanguage = systemConfiguration.locale.getLanguage();
            }
            preferencesLanguage = systemLanguage;
        }

        // Update preferences (variable language)
        preferences.edit().putString("language_temp", preferencesLanguage).apply();

        // Log.d("Logs", "Set new language = " + preferencesLanguage);

        // Set new language
        Resources resources = context.getResources();
        Configuration configuration = context.getResources().getConfiguration();

        Locale locale = new Locale(preferencesLanguage);
        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, null);
        }
    }
}
