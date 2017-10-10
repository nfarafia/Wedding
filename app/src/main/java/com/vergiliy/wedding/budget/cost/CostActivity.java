package com.vergiliy.wedding.budget.cost;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.ZoomOutPageTransformer;
import com.vergiliy.wedding.budget.payment.PaymentsFragment;

public class CostActivity extends BaseActivity {

    ViewPager viewPager;
    private final ViewGroup nullGroup = null;

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
    }

    // Listener clicks on page categories
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            String text = getResources().getString(R.string.test_cost_page, position);
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
        setTitle(R.string.activity_cost_title);
        setContentView(R.layout.activity_cost);
        // Change activities with animation
        overridePendingTransition(R.anim.create_slide_in, R.anim.create_slide_out);

        // Creating FloatingButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit);
        // fab.setOnClickListener();

        // Show back button in ActionBar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Create ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new PageChangeListener());
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());

        // Show TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Get last tab for add custom view
        TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getTabCount() - 1);
        if (tab != null) {
            tab.setCustomView(prepareCustomView(tab));
        }
    }

    // Prepare custom View with notification counter
    private View prepareCustomView(TabLayout.Tab tab) {
        View view = getLayoutInflater().inflate(R.layout.tab_custom_view, nullGroup);
        TextView tv_title = view.findViewById(R.id.tab_title);
        TextView tv_count = view.findViewById(R.id.tab_count);
        tv_title.setText(tab.getText());
        tv_count.setText("0");

        return view;
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
}
