package com.vergiliy.wedding.checklist.subtask;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.BaseListener;

import static com.vergiliy.wedding.checklist.ChecklistRecyclerAdapter.actionMode;

// Creating Subtask Dialog
public class SubtaskDialogListener extends BaseListener implements View.OnClickListener {

    private Subtask subtask = null;

    // Get subtask from main class ChecklistRecyclerAdapter
    SubtaskDialogListener(Subtask subtask) {
        this.subtask = subtask;
    }

    // Default constructor
    public SubtaskDialogListener() {}

    @Override
    public void onClick(View view) {
        BaseActivity activity = getActivity(view);

        // Close ActionMode if it was open
        if (actionMode != null) {
            actionMode.finish();
        }

        if (activity != null) {
            FragmentManager manager = activity.getSupportFragmentManager();
            SubtaskDialogFragment fragment = (SubtaskDialogFragment) manager
                            .findFragmentByTag(SubtaskDialogFragment.class.getSimpleName());
            if (fragment == null) {
                fragment = SubtaskDialogFragment.newInstance(subtask);
                fragment.show(manager, SubtaskDialogFragment.class.getSimpleName());
            }
        }
    }
}