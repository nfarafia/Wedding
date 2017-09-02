package com.vergiliy.wedding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

// Splash screen; open before load MainActivity
public class SplashActivity extends AppCompatActivity {

    final int SPLASH_DISPLAY = 2000; // Duration splash screen
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show splash scree duration several seconds then start MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY);
    }
}