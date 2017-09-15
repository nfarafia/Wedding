package com.vergiliy.wedding.guests;

import android.os.Bundle;
import android.view.MenuItem;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.R;

public class GuestsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Replace FrameLayout on our activity layout
        getLayoutInflater().inflate(R.layout.contant_guests, frameLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check current activity in the NavigationDrawer
        MenuItem menuItem =  navigationView.getMenu().findItem(R.id.menu_general_guests)
                .setChecked(true);
    }
}
