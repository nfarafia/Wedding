package com.vergiliy.wedding.budget;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.vergiliy.wedding.NavigationActivity;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.budget.category.Category;
import com.vergiliy.wedding.budget.category.CategoryDatabase;
import com.vergiliy.wedding.budget.category.CategoryProcessing;
import com.vergiliy.wedding.budget.cost.CostDatabase;
import com.vergiliy.wedding.budget.cost.CostDialogListener;

import java.util.ArrayList;
import java.util.List;

import static com.vergiliy.wedding.budget.BudgetRecyclerAdapter.actionMode;

public class BudgetActivity extends NavigationActivity implements BudgetInterface {

    protected static ViewPager viewPager;

    private CostDatabase db_main;
    protected CategoryDatabase db_category;

    private List<Category> categories = new ArrayList<>();

    // Create ViewPagerAdapter
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return BudgetFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return categories.get(position).getLocaleName();
        }

        // For update fragment when call notifyDataSetChanged();
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    // Listener clicks on page categories
    private class PageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            // Close ActionMode if it was open
            if (actionMode != null) {
                actionMode.finish();
            }
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
        setTitle(R.string.activity_budget_title);
        // Replace FrameLayout on our activity layout
        getLayoutInflater().inflate(R.layout.contant_budget, frameLayout);

        // Create new CostDatabase and CategoryDatabase
        db_main = new CostDatabase(this);
        db_category = new CategoryDatabase(this);

        // Get categories from database
        categories = db_category.getAll();
        if (categories.size() == 0 ) {
            Toast.makeText(getApplicationContext(), R.string.budget_title_none,
                    Toast.LENGTH_LONG).show();
        }

        // Create ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new PageChangeListener());

        // Show TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.VISIBLE);

        // Add padding to TabLayout
        int padding_right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
                getResources().getDisplayMetrics());
        tabLayout.setPadding(0,0,padding_right,0);

        // Show button Add for add new tab
        ImageButton tabAdd = (ImageButton) findViewById(R.id.budget_tabs_edit);
        tabAdd.setImageResource(R.drawable.ic_tabs_edit);
        tabAdd.setVisibility(View.VISIBLE);

        tabAdd.setOnClickListener(new CategoryProcessing());

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
                Toast toast = Toast.makeText(getApplicationContext(), view.getContentDescription(),
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.END,
                        metrics.widthPixels - location[0],
                        location[1] + view.getHeight()/2);
                toast.show();

                return true;
            }
        });

        // Creating FloatingButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new CostDialogListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check current activity in the NavigationDrawer
        MenuItem menuItem =  navigationView.getMenu().findItem(R.id.menu_general_budget)
                .setChecked(true);

        // Update current fragment
        getViewPager().getAdapter().notifyDataSetChanged();
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
        if (db_category != null) {
            db_category.close();
        }
        viewPager = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Close ActionMode if it was open
        if (actionMode != null) {
            actionMode.finish();
        }
    }

    // Get db_main
    public CostDatabase getDbCost() {
        return db_main;
    }

    // Get viewPager
    public ViewPager getViewPager() {
        return viewPager;
    }


    // Get categories id by position
    public int getCategoryIdByPosition(int position) {
        return categories.get(position).getId();
    }

    // Get categories
    public List<Category> getCategories() {
        return categories;
    }
}
