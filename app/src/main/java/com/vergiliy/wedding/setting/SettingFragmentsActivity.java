package com.vergiliy.wedding.setting;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.R;

public class SettingFragmentsActivity extends BaseActivity {

    PreferenceFragment fragment;
    Integer position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_fragments);

        // Receive extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("position");
            // Open called fragment
            switch(position) {
                case 0:
                    fragment = new SettingsProfileFragment();
                    break;
                case 1:
                    fragment = new SettingsWeddingFragment();
                    break;
                case 2:
                    fragment = new SettingsOtherFragment();
                    break;
            }

            if (fragment != null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_content, fragment)
                        .commit();
            } else {
                finish();
            }
        } else {
            finish();
        }

        // Show back button in ActionBar
        if (getSupportActionBar() != null) {
            String[] list = getResources().getStringArray(R.array.settings_list_entries);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(list[position]);
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("class", SettingFragmentsActivity.class.getSimpleName());
        setResult(RESULT_OK, intent);
        super.finish();
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
}