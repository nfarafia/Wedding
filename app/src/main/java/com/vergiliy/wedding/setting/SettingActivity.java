package com.vergiliy.wedding.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.R;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_setting_title);
        setContentView(R.layout.activity_setting);

        Bundle extras = getIntent().getExtras();
        if (extras == null || extras.getBoolean("animation", true)) {
            // Change activities with animation
            overridePendingTransition(R.anim.create_slide_in, R.anim.create_slide_out);
        }

        // Получаем элемент ListView
        ListView listView = (ListView) findViewById(R.id.settingsList);

        // Open selected Fragment from SettingFragmentsActivity
        listView.setOnItemClickListener(new onItemClickListener());

        // Show back button in ActionBar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private class onItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            switch (position) {
                case 0:
                case 1:
                case 2:
                    Intent intent = new Intent(getApplicationContext(),
                            SettingFragmentsActivity.class);
                    intent.putExtra("position", position); // Transfer id
                    startActivity(intent);
                    break;
            }
        }
    }
}