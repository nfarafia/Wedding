package com.vergiliy.wedding.setting;

import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import com.vergiliy.wedding.R;

public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Change activities with animation
        overridePendingTransition(R.anim.create_slide_in, R.anim.create_slide_out);

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list)
                .getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this)
                .inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.settings_header, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return SettingsFragment.class.getName().equals(fragmentName)
                || SettingsOtherFragment.class.getName().equals(fragmentName);
    }
}
