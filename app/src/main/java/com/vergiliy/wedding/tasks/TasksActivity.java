package com.vergiliy.wedding.tasks;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;

import com.vergiliy.wedding.NavigationActivity;
import com.vergiliy.wedding.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TasksActivity extends NavigationActivity {

    static final int PAGE_COUNT = 13;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    static final String wedding_date_string = "2018-05-12 14:00";
    // static Date wedding_date = new Date(1526122800000L); // Saturday, May 12, 2018 2:00:00 PM GMT+03:00
    Date wedding_date;
    Calendar calendar;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_tasks_title);
        // Replace FrameLayout on our activity layout
        getLayoutInflater().inflate(R.layout.contant_tasks, frameLayout);

        // Get Date from string
        try {
            wedding_date = format.parse(wedding_date_string);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
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

    @Override
    protected void onResume() {
        super.onResume();
        // Check current activity in the NavigationDrawer
        MenuItem menuItem =  navigationView.getMenu().findItem(R.id.menu_general_tasks)
                .setChecked(true);
    }

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

    // Класс, который обрабатывает нажатия на
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

    // PageTransformer
    private class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.9f;
        private static final float MIN_ALPHA = 0.7f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            // This page is way off-screen to the left.
            if (position < -1) { // [-Infinity,-1)
                view.setAlpha(0);

            // Modify the default slide transition to shrink the page as well
            } else if (position <= 1) { // [-1,1]

                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            // This page is way off-screen to the right.
            } else { // (1,+Infinity]
                view.setAlpha(0);
            }
        }
    }
}
