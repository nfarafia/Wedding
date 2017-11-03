package com.vergiliy.wedding.tasks;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.vergiliy.wedding.NavigationActivity;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.ZoomOutPageTransformer;
import com.vergiliy.wedding.setting.DatePreference;

import java.util.Calendar;
import java.util.Date;

public class TasksActivity extends NavigationActivity {

    static final int PAGE_COUNT = 13;

    Date wedding_date;
    Calendar calendar;

    ViewPager viewPager;

    // Create ViewPagerAdapter
    private class ViewPagerAdapter extends FragmentStatePagerAdapter{
        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return TasksFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            calendar.add(Calendar.MONTH, 1); // Прибавляем по месяцу
            return DateFormat.format("LLLL yyyy", calendar);
        }
    }

    // Listener clicks by page title
    private class PageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            String text = getResources().getString(R.string.test_tasks_page, position);
            Snackbar.make(viewPager, text, Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_tasks_title);
        // Replace FrameLayout on our activity layout
        getLayoutInflater().inflate(R.layout.contant_tasks, frameLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check current activity in the NavigationDrawer
        MenuItem menuItem =  navigationView.getMenu().findItem(R.id.menu_general_tasks)
                .setChecked(true);

        // Get wedding date from preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String text = preferences.getString("date", null);

        // Get Date from string
        if (text != null) {
            try {
                wedding_date = DatePreference.DATA_FORMAT.parse(text);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        } else {
            wedding_date = new Date();
        }

        calendar = Calendar.getInstance();
        calendar.setTime(wedding_date);
        // Отнимаем год от текущей даты свадьбы (перехлестом на месяц)
        calendar.add(Calendar.MONTH, -PAGE_COUNT + 1);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new PageChangeListener());
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.VISIBLE);
    }
}
