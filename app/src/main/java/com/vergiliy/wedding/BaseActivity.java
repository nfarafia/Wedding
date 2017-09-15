package com.vergiliy.wedding;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.vergiliy.wedding.main.MainActivity;
import com.vergiliy.wedding.coasts.CoastsActivity;
import com.vergiliy.wedding.countdown.CountdownActivity;
import com.vergiliy.wedding.guests.GuestsActivity;
import com.vergiliy.wedding.setting.SettingActivity;
import com.vergiliy.wedding.tasks.TasksActivity;
import com.vergiliy.wedding.vendors.VendorsActivity;
import com.vergiliy.wedding.info.AboutActivity;
import com.vergiliy.wedding.info.FeedbackActivity;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected NavigationView navigationView;
    protected FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Support ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        // Make NavigationDrawer toggle
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // Close NavigationDrawer when press button back
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Switch activity from NavigationDrawer
        switch(id) {
            case R.id.menu_general_countdown:
                startActivity(new Intent(this, CountdownActivity.class));
                break;
            case R.id.menu_general_tasks:
                startActivity(new Intent(this, TasksActivity.class));
                break;
            case R.id.menu_general_coasts:
                startActivity(new Intent(this, CoastsActivity.class));
                break;
            case R.id.menu_general_guests:
                startActivity(new Intent(this, GuestsActivity.class));
                break;
            case R.id.menu_general_vendors:
                startActivity(new Intent(this, VendorsActivity.class));
                break;
            case R.id.menu_addition_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.menu_info_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.menu_info_feedback:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.menu_info_rate:
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id="
                                    + appPackageName)));
                }
                break;
            default:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

        // Close NavigationDrawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.START, false);
        return true;
    }
}
