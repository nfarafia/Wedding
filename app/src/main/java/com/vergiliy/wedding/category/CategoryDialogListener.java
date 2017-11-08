package com.vergiliy.wedding.category;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.BaseListener;

import static com.vergiliy.wedding.category.CategoryRecyclerAdapter.actionMode;

// Creating Category Dialog
class CategoryDialogListener extends BaseListener implements View.OnClickListener {

    private Category category = null;

    // Get category from main class CategoryRecyclerAdapter
    CategoryDialogListener(Category category) {
        this.category = category;
    }

    // Default constructor
    CategoryDialogListener() {}

    @Override
    public void onClick(View view) {
        BaseActivity activity = getActivity(view);

        // Close ActionMode if it was open
        if (actionMode != null) {
            actionMode.finish();
        }

        if (activity != null) {
            FragmentManager manager = activity.getSupportFragmentManager();
            CategoryDialogFragment fragment = (CategoryDialogFragment) manager
                            .findFragmentByTag(CategoryDialogFragment.class.getSimpleName());
            if (fragment == null) {
                fragment = CategoryDialogFragment.newInstance(category);
                fragment.show(manager, CategoryDialogFragment.class.getSimpleName());
            }
        }
    }
}