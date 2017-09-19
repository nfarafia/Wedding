package com.vergiliy.wedding.countdown;

import android.os.Bundle;
import android.view.MenuItem;

import com.vergiliy.wedding.NavigationActivity;
import com.vergiliy.wedding.R;

public class CountdownActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_countdown_title);
        // Replace FrameLayout on our activity layout
        getLayoutInflater().inflate(R.layout.contant_countdown, frameLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check current activity in the NavigationDrawer
        MenuItem menuItem =  navigationView.getMenu().findItem(R.id.menu_general_countdown)
                .setChecked(true);
    }
}
