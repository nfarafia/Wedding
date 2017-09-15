package com.vergiliy.wedding.main;

import android.os.Bundle;

import com.vergiliy.wedding.R;

public class MainActivity extends com.vergiliy.wedding.BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Replace FrameLayout on our activity layout
        getLayoutInflater().inflate(R.layout.content_main, frameLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Uncheck all items
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }
}
