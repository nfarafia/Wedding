package com.vergiliy.wedding.tasks;

import android.os.Bundle;
import android.view.MenuItem;

import com.vergiliy.wedding.MainActivity;
import com.vergiliy.wedding.R;

public class TasksActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.contant_tasks, frameLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check current activity in the navigation drawer
        MenuItem menuItem =  navigationView.getMenu().findItem(R.id.menu_general_tasks).setChecked(true);
    }
}
