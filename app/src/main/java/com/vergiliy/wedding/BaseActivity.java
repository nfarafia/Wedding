package com.vergiliy.wedding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class BaseActivity extends AppCompatActivity {

    private BaseLocale locale;
    private String language = null;

    protected SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        locale = new BaseLocale(this);
        language = locale.getLanguage(); // Save current language

        // Get language before flip screen
        if (savedInstanceState != null) {
            String language_temp = savedInstanceState.getString("language", null);
            // Restore current language
            if (language_temp != null && !language.equals(language_temp)) {
                language = locale.setLocale(); // Set chosen locale (and save return value)
            }
        // Checking language if it start screen
        } else if (bundle != null && bundle.getBoolean("start")
                && locale.isChangeLanguage(language)) {
            locale.setLocale(); // Set chosen locale
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Hide View with shadow when if the device you are running is >= to Lollipop (API 21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View shadow = findViewById(R.id.toolbar_shadow);
            if (shadow != null) {
                shadow.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (locale.isChangeLanguage(language)) {
            Intent intent = getIntent();
            overridePendingTransition(0,0);
            intent.putExtra("animation", false);
            finish();
            startActivity(intent);
        }
    }

    // Save current language for screen flip
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("language", language);
    }

    public BaseLocale getLocaleClass() {
        return locale;
    }

    public String getLanguage() {
        return language;
    }
}
