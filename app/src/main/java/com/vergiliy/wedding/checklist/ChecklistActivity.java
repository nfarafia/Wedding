package com.vergiliy.wedding.checklist;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vergiliy.wedding.NavigationActivity;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.category.Category;
import com.vergiliy.wedding.category.CategoryActivity;
import com.vergiliy.wedding.category.CategoryDatabase;
import com.vergiliy.wedding.checklist.summary.SummaryActivity;
import com.vergiliy.wedding.checklist.task.TaskDatabase;
import com.vergiliy.wedding.checklist.task.TaskDialogListener;
import com.vergiliy.wedding.setting.SettingFragmentsActivity;

import java.util.ArrayList;
import java.util.List;

import static com.vergiliy.wedding.checklist.ChecklistRecyclerAdapter.actionMode;

public class ChecklistActivity extends NavigationActivity implements ChecklistInterface {

    protected static ViewPager viewPager;

    private TaskDatabase db_task;
    protected CategoryDatabase db_category;

    private List<Category> categories = new ArrayList<>();

    // Create ViewPagerAdapter
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return ChecklistFragment.newInstance(position);
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
        setTitle(R.string.activity_checklist_title);
        // Replace FrameLayout on our activity layout
        getLayoutInflater().inflate(R.layout.contant_checklist, frameLayout);

        // Create new TaskDatabase, SubtaskDatabase and CategoryDatabase
        db_task = new TaskDatabase(this);
        db_category = new CategoryDatabase(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Get categories from database
        updateCategories();

        // Create ViewPager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new PageChangeListener());

        // Show TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Add padding to TabLayout
        int padding_right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
                getResources().getDisplayMetrics());
        tabLayout.setPadding(0,0,padding_right,0);

        // Show CategoryActivity
        View.OnClickListener budgetListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                startActivityForResult(intent, 1);
            }
        };

        // Show button Add for add new tab
        ImageButton tabEdit = (ImageButton) findViewById(R.id.category_tab_edit);
        tabEdit.setImageResource(R.drawable.ic_category_tab_edit);
        tabEdit.setVisibility(View.VISIBLE);
        tabEdit.setOnClickListener(budgetListener);

        Button tabAdd = (Button) findViewById(R.id.category_button_add);
        tabAdd.setOnClickListener(budgetListener);

        // Show toast when long click button
        tabEdit.setOnLongClickListener(new View.OnLongClickListener() {

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
        fab.setOnClickListener(new TaskDialogListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check current activity in the NavigationDrawer
        MenuItem menuItem =  navigationView.getMenu().findItem(R.id.menu_general_checklist)
                .setChecked(true);

        // Update current fragment
        if (getViewPager() != null) {
            getViewPager().getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        // Refresh TabLayout when return from CategoryActivity
        if (data.getStringExtra("class").equals(CategoryActivity.class.getSimpleName())) {
            // Update categories from database
            updateCategories();
        }
    }

    // Create top context menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.checklist_action_menu, menu);

        return true;
    }

    // Top context menu listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Switch activity from NavigationDrawer
        switch(id) {
            // Open new Activity when click Summary icon
            case R.id.menu_action_summary:
                startActivity(new Intent(this, SummaryActivity.class));
                break;
            // Open SettingsWeddingFragment to edit wedding date
            case R.id.menu_action_setting:
                Intent intent = new Intent(getApplicationContext(), SettingFragmentsActivity.class);
                intent.putExtra("position", 1); // Transfer id
                startActivityForResult(intent, 1);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    // Close Database connection when activity destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db_category != null) {
            db_category.close();
        }
        if (db_task != null) {
            db_task.close();
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

    // Get categories from database
    private void updateCategories() {
        categories = db_category.getAll();

        TabLayout tab = (TabLayout) findViewById(R.id.tabs);
        LinearLayout noneText = (LinearLayout) findViewById(R.id.category_none);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);

        if (categories.size() == 0) {
            viewPager.setVisibility(View.GONE);
            tab.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            noneText.setVisibility(View.VISIBLE);
        } else {
            viewPager.setVisibility(View.VISIBLE);
            tab.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
            noneText.setVisibility(View.GONE);
        }
    }

    // Get db_task
    public TaskDatabase getDbTask() {
        return db_task;
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
