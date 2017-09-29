package com.vergiliy.wedding.coasts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.vergiliy.wedding.NavigationActivity;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class CoastsActivity extends NavigationActivity {

    protected ViewPager viewPager;

    private CoastsDatabase db_main;
    protected CoastsSectionsDatabase db_sections;

    private List<CoastsSection> sections = new ArrayList<>();

    // Create ViewPagerAdapter
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return CoastsFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return sections.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return sections.get(position).getName();
        }

        // For update fragment when call notifyDataSetChanged();
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    // Listener clicks on page sections
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
        setTitle(R.string.activity_coasts_title);
        // Replace FrameLayout on our activity layout
        getLayoutInflater().inflate(R.layout.contant_coasts, frameLayout);

        // Create new CoastsDatabase and CoastsSectionsDatabase
        db_main = new CoastsDatabase(this);
        db_sections = new CoastsSectionsDatabase(this);

        // Get sections from database
        sections = db_sections.getAll();
        if (sections.size() == 0 ) {
            Toast.makeText(getApplicationContext(), R.string.coast_title_none,
                    Toast.LENGTH_LONG).show();
        }

        // Creating FloatingButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new CoastProcessing());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check current activity in the NavigationDrawer
        MenuItem menuItem =  navigationView.getMenu().findItem(R.id.menu_general_coasts)
                .setChecked(true);

        // Create ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new PageChangeListener());
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        // Show TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.VISIBLE);

        // Show button Add for add new tab
        ImageButton tabAdd = (ImageButton) findViewById(R.id.tabs_add);
        tabAdd.setImageResource(R.drawable.ic_tab_edit);
        tabAdd.setVisibility(View.VISIBLE);
        tabAdd.setOnClickListener(new CoastSectionProcessing());

        // Show toast when long click button
        tabAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Get Viw position
                int[] location = new int[2];
                view.getLocationOnScreen(location);

                // Get screen width
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);

                // Create the toast in found coordinates
                Toast toast= Toast.makeText(getApplicationContext(), view.getContentDescription(),
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.END,
                        metrics.widthPixels - location[0],
                        location[1] + view.getHeight()/2);
                toast.show();

                return true;
            }
        });
    }

    // Create top context menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    // Top context menu listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Display Toasts when pressed button action_settings in top context menu
        if (id == R.id.action_settings) {
            Toast.makeText(this, R.string.test_menu_setting_click,
                    Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Close Database connection when activity destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db_main != null) {
            db_main.close();
        }
        if (db_sections != null) {
            db_sections.close();
        }
    }

    // Get db_main
    public CoastsDatabase getDbMain() {
        return db_main;
    }

    // Get viewPager
    public ViewPager getViewPager() {
        return viewPager;
    }


    // Get sections id by position
    public int getSectionIdByPosition(int position) {
        return sections.get(position).getId();
    }

    // Get sections
    public List<CoastsSection> getSections() {
        return sections;
    }
}
