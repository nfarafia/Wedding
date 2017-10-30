package com.vergiliy.wedding.budget.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.budget.cost.CostDatabase;

import java.util.Collections;
import java.util.List;

import static com.vergiliy.wedding.budget.category.CategoryRecyclerAdapter.actionMode;

public class CategoryActivity extends BaseActivity {

    private CostDatabase db_cost;
    private CategoryDatabase db_category;

    private static RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;
    private List<Category> all;

    private class CategoriesTouchHelper extends ItemTouchHelper.Callback {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            final int fromPosition = viewHolder.getAdapterPosition();
            final int toPosition = target.getAdapterPosition();

            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(all, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(all, i, i - 1);
                }
            }
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}

        // Defines the enabled move directions in each state (idle, swiping, dragging)
        @Override
        public int getMovementFlags(RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder) {
            return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                    ItemTouchHelper.DOWN | ItemTouchHelper.UP);
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);

            // Save new position
            for (int i = 0; i < all.size(); i++) {
                Category category =  all.get(i);
                category.setPosition(i);
                getDbCategory().update(category);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_budget_category_title);
        setContentView(R.layout.activity_budget_category);
        // Change activities with animation
        overridePendingTransition(R.anim.create_slide_in, R.anim.create_slide_out);

        db_cost = new CostDatabase(this);
        db_category = new CategoryDatabase(this);

        recyclerView = (RecyclerView) findViewById(R.id.categories_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Fill RecyclerView
        fillRecyclerView();

        // Implement reordering category
        itemTouchHelper = new ItemTouchHelper(new CategoriesTouchHelper());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Creating FloatingButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new CategoryDialogListener());

        // Show back button in ActionBar
        if (getSupportActionBar() != null) {
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

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("class", CategoryActivity.class.getSimpleName());
        setResult(RESULT_OK, intent);
        super.finish();
    }

    // Close Database connection when activity destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db_cost != null) {
            db_cost.close();
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

    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    public void fillRecyclerView() {
        all = db_category.getAll();
        TextView noneText = (TextView) findViewById(R.id.categories_list_none);

        if (all.size() > 0) {
            RecyclerView.Adapter adapter = new CategoryRecyclerAdapter(this, all);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            noneText.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noneText.setVisibility(View.VISIBLE);
        }
    }

    public CostDatabase getDbCost() {
        return db_cost;
    }

    public CategoryDatabase getDbCategory() {
        return db_category;
    }
}
