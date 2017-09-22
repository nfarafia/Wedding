package com.vergiliy.wedding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public Language languageClass = new Language(this);
    public String language = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        language = languageClass.getLanguage(); // Save current language
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (languageClass.isChangeLanguage(language)) {
            Intent intent = getIntent();
            overridePendingTransition(0,0);
            intent.putExtra("animation", false);
            finish();
            startActivity(intent);
        }
    }
}
