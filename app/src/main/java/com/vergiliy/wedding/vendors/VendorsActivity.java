package com.vergiliy.wedding.vendors;

import android.os.Bundle;
import android.view.MenuItem;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.R;

public class VendorsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.contant_vendors, frameLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check current activity in the navigation drawer
        MenuItem menuItem =  navigationView.getMenu().findItem(R.id.menu_general_vendors).setChecked(true);
    }
}
