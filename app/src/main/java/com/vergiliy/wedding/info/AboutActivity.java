package com.vergiliy.wedding.info;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.R;

import static android.R.attr.name;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_about_title);
        setContentView(R.layout.activity_about);
        // Change activities with animation
        overridePendingTransition(R.anim.create_slide_in, R.anim.create_slide_out);

        // Show back button in ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }




        FragmentManager manager = getSupportFragmentManager();
        MyDialogFragment fragment = (MyDialogFragment) manager.findFragmentByTag("MyDialog");
        if (fragment == null) {
            fragment = new MyDialogFragment();
            fragment.show(manager, "MyDialog");
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Return to previous activity; call method onBackPressed
            case android.R.id.home:
                onBackPressed();
                return true;
            // If another id, call super method
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Show previous activity with animation
        overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
    }
}
