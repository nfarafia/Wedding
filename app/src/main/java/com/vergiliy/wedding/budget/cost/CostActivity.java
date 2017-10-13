package com.vergiliy.wedding.budget.cost;

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
import android.view.ViewGroup;
import android.widget.TextView;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.ZoomOutPageTransformer;
import com.vergiliy.wedding.budget.BudgetInterface;
import com.vergiliy.wedding.budget.category.Category;
import com.vergiliy.wedding.budget.category.CategoryDatabase;
import com.vergiliy.wedding.budget.payment.Payment;
import com.vergiliy.wedding.budget.payment.PaymentDatabase;
import com.vergiliy.wedding.budget.payment.PaymentDialogListener;
import com.vergiliy.wedding.budget.payment.PaymentsFragment;

import java.util.ArrayList;
import java.util.List;

import static com.vergiliy.wedding.budget.payment.PaymentsRecyclerAdapter.actionMode;

public class CostActivity extends BaseActivity implements BudgetInterface {

    ViewPager viewPager;
    TabLayout tabLayout;
    FloatingActionButton fabEdit, fabAdd;
    private final ViewGroup nullGroup = null;

    private CostDatabase db_cost;
    private PaymentDatabase db_payment;
    private CategoryDatabase db_category;

    private Cost cost = null;
    private List<Payment> payments =  new ArrayList<>();
    private List<Category> categories = new ArrayList<>();

    // Create ViewPagerAdapter
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        final String[] list;

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            list = getResources().getStringArray(R.array.cost_tabs);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CostFragment(); // Show CostFragment
                case 1:
                    return new PaymentsFragment(); // Show PaymentsFragment
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
            cost = db_cost.getOne(cost.getId()); // Update Cost
            payments = db_payment.getAllByIdCost(cost.getId()); // Update all payments for current cost

            super.notifyDataSetChanged();

            // Set custom view for last item
            setCustomViewForLastItem(payments.size());
        }
    }

    // Listener clicks on page categories
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            // Change active Fab (for edit cost or add new payment)
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
        setContentView(R.layout.activity_cost);
        // Change activities with animation
        overridePendingTransition(R.anim.create_slide_in, R.anim.create_slide_out);

        db_cost = new CostDatabase(this);
        db_payment = new PaymentDatabase(this);
        db_category = new CategoryDatabase(this);

        // Get cost from extras id
        Bundle bundle = getIntent().getExtras();
        Integer id = bundle.getInt("id", -1);
        if (!id.equals(-1)) {
            cost = db_cost.getOne(id);
        }
        if (cost == null) {
            onBackPressed(); // If Id not found, return back
            return;
        }

        // Get all payments for current cost
        payments = db_payment.getAllByIdCost(cost.getId());

        // Put name to Title
        setTitle(cost.getLocaleName());

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
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());

        // Show TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setCustomViewForLastItem(payments.size()); // Set custom view for last item

        // Creating FloatingButton
        fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabEdit.setOnClickListener(new CostDialogListener(cost));
        fabAdd.setOnClickListener(new PaymentDialogListener());

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
        if (db_cost != null) {
            db_cost.close();
        }
        if (db_payment != null) {
            db_payment.close();
        }
        if (db_category != null) {
            db_category.close();
        }
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
    public CostDatabase getDbCost() {
        return db_cost;
    }

    public PaymentDatabase getDbPayment() {
        return db_payment;
    }

    @Override
    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    public Cost getCost() {
        return cost;
    }

    public List<Payment> getPayments() {
        return payments;
    }
}
