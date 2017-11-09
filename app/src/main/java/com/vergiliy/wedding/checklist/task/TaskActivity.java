package com.vergiliy.wedding.checklist.task;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.category.Category;
import com.vergiliy.wedding.category.CategoryDatabase;
import com.vergiliy.wedding.checklist.ChecklistInterface;
import com.vergiliy.wedding.checklist.subtask.Subtask;
import com.vergiliy.wedding.checklist.subtask.SubtaskDatabase;
import com.vergiliy.wedding.checklist.subtask.SubtaskDialogListener;
import com.vergiliy.wedding.checklist.subtask.SubtaskFragment;

import java.util.ArrayList;
import java.util.List;

import static com.vergiliy.wedding.checklist.ChecklistRecyclerAdapter.actionMode;

public class TaskActivity extends BaseActivity implements ChecklistInterface {

    static ViewPager viewPager;
    TabLayout tabLayout;
    FloatingActionButton fabEdit, fabAdd;

    private TaskDatabase db_task;
    private SubtaskDatabase db_subtask;
    private CategoryDatabase db_category;

    private Task task = null;
    private List<Subtask> subtasks =  new ArrayList<>();
    private List<Category> categories = new ArrayList<>();

    // Create ViewPagerAdapter
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        final String[] list;

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            list = getResources().getStringArray(R.array.task_tabs);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TaskFragment(); // Show TaskFragment
                case 1:
                    return new SubtaskFragment(); // Show SubtaskFragment
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list[position];
        }

        // For update fragment when call notifyDataSetChanged();
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void notifyDataSetChanged() {
            task = db_task.getOne(task.getId()); // Update Task
            subtasks = db_subtask.getAllByIdTask(task.getId()); // Update all subtasks for current task
            fabEdit.setOnClickListener(new TaskDialogListener(task));

            super.notifyDataSetChanged();

            // Set custom view for last item
            setCustomViewForLastItem(subtasks.size());
        }
    }

    // Listener clicks on page categories
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            // Change active Fab (for edit task or add new subtask)
            switch (position) {
                case 0:
                    fabEdit.setVisibility(View.VISIBLE);
                    fabAdd.setVisibility(View.GONE);
                    break;
                case 1:
                    fabEdit.setVisibility(View.GONE);
                    fabAdd.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // Close ActionMode if it was open
            if (actionMode != null) {
                actionMode.finish();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        // Change activities with animation
        overridePendingTransition(R.anim.create_slide_in, R.anim.create_slide_out);

        db_task = new TaskDatabase(this);
        db_subtask = new SubtaskDatabase(this);
        db_category = new CategoryDatabase(this);

        // Get task from extras id
        Bundle bundle = getIntent().getExtras();
        Integer id = bundle.getInt("id", -1);
        if (!id.equals(-1)) {
            task = db_task.getOne(id);
        }
        if (task == null) {
            onBackPressed(); // If Id not found, return back
            return;
        }

        // Get all subtasks for current task
        subtasks = db_subtask.getAllByIdTask(task.getId());

        // Put name to Title
        setTitle(task.getLocaleName());

        // Get categories from database
        categories = db_category.getAll();

        // Support ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new PageChangeListener());
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());

        // Show TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setCustomViewForLastItem(subtasks.size()); // Set custom view for last item

        // Creating FloatingButton
        fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabEdit.setOnClickListener(new TaskDialogListener(task));
        fabAdd.setOnClickListener(new SubtaskDialogListener());

        // Show back button in ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // Set custom View with notification counter for last item
    private void setCustomViewForLastItem(int count) {
        TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getTabCount() - 1);

        if (tab != null) {
            View view = getLayoutInflater().inflate(R.layout.tab_custom_view, nullGroup);
            TextView tab_title = view.findViewById(R.id.tab_title);
            TextView tab_count = view.findViewById(R.id.tab_count);
            tab_title.setText(tab.getText());
            tab_count.setText(getString(R.string.count, count));
            tab.setCustomView(view);
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
        if (db_subtask != null) {
            db_subtask.close();
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

    @Override
    public TaskDatabase getDbTask() {
        return db_task;
    }

    public SubtaskDatabase getDbSubtask() {
        return db_subtask;
    }

    @Override
    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    public Task getTask() {
        return task;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }
}
