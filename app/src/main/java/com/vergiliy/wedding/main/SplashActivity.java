package com.vergiliy.wedding.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.vergiliy.wedding.BaseActivity;

import com.vergiliy.wedding.R;

// Splash screen; open before load MainActivity
public class SplashActivity extends BaseActivity {

    public static final int DURATION_SPLASH = 5000;
    public static final int DURATION_LOGO = 1000;
    public static final int DURATION_BUTTON = 1000;
    public static final int DURATION_OTHER = 500;
    public static final int DELAY_START = 200;
    public static final int DELAY_ITEM = 300;
    public static final int DELAY_BETWEEN = 500;

    public final Context context = this;

    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        // Go to fullscreen without navigation bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Replace current language
        if (languageClass.isChangeLanguage(language)) {
            languageClass.setLocale(); // Set chosen locale
        }

        setContentView(R.layout.activity_splash);
        animate(); // Start animation

        // Show splash screen duration several seconds then start NavigationActivity
        handler.postDelayed(runnable, DURATION_SPLASH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Interrupt thread
        handler.removeCallbacks(runnable);
    }

    // Jump to NavigationActivity (call when the button clicked)
    public void start(View view){
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Animation elements
    private void animate() {
        ImageView logoImageView = (ImageView) findViewById(R.id.splash_logo);
        ViewGroup container = (ViewGroup) findViewById(R.id.splash_container);

        // Animation logo
        ViewCompat.animate(logoImageView)
                .translationY(-250)
                .scaleX(3)
                .scaleY(3)
                .setStartDelay(DELAY_START)
                .setDuration(DURATION_LOGO)
                .setInterpolator(new DecelerateInterpolator(1.2f))
                .start();

        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;

            // Animation buttons
            if (!(view instanceof Button)) {
                viewAnimator = ViewCompat.animate(view)
                        .translationY(50)
                        .alpha(1)
                        .setStartDelay((DELAY_ITEM * i) + DELAY_BETWEEN)
                        .setDuration(DURATION_BUTTON);

            // Animation another elements
            } else {
                viewAnimator = ViewCompat.animate(view)
                        .scaleY(1)
                        .scaleX(1)
                        .setStartDelay((DELAY_ITEM * i) + DELAY_BETWEEN)
                        .setDuration(DURATION_OTHER);
            }

            viewAnimator.setInterpolator(new DecelerateInterpolator()).start();
        }
    }
}