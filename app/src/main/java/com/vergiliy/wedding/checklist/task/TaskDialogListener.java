package com.vergiliy.wedding.checklist.task;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.BaseListener;

import static com.vergiliy.wedding.checklist.ChecklistRecyclerAdapter.actionMode;

// Creating Task Dialog
public class TaskDialogListener extends BaseListener implements View.OnClickListener {

    private Task task = null;

    // Get task from main class ChecklistRecyclerAdapter
    public TaskDialogListener(Task task) {
        this.task = task;
    }

    // Default constructor
    public TaskDialogListener() {}

    @Override
    public void onClick(View view) {
        BaseActivity activity = getActivity(view);

        // Close ActionMode if it was open
        if (actionMode != null) {
            actionMode.finish();
        }

        if (activity != null) {
            FragmentManager manager = activity.getSupportFragmentManager();
            TaskDialogFragment fragment =
                    (TaskDialogFragment) manager.findFragmentByTag(TaskDialogFragment.class.getSimpleName());
            if (fragment == null) {
                fragment = TaskDialogFragment.newInstance(task);
                fragment.show(manager, TaskDialogFragment.class.getSimpleName());
            }
        }
    }
}