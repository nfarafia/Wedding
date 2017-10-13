package com.vergiliy.wedding.budget.cost;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.BaseListener;
import com.vergiliy.wedding.budget.payment.PaymentDialogFragment;

import static com.vergiliy.wedding.budget.BudgetRecyclerAdapter.actionMode;

// Creating Cost Dialog
public class CostDialogListener extends BaseListener implements View.OnClickListener {

    private Cost cost = null;

    // Get cost from main class BudgetRecyclerAdapter
    public CostDialogListener(Cost cost) {
        this.cost = cost;
    }

    // Default constructor
    public CostDialogListener() {}

    @Override
    public void onClick(View view) {
        BaseActivity activity = getActivity(view);

        // Close ActionMode if it was open
        if (actionMode != null) {
            actionMode.finish();
        }

        if (activity != null) {
            FragmentManager manager = activity.getSupportFragmentManager();
            CostDialogFragment fragment =
                    (CostDialogFragment) manager.findFragmentByTag(CostDialogFragment.class.getSimpleName());
            if (fragment == null) {
                fragment = CostDialogFragment.newInstance(cost);
                fragment.show(manager, CostDialogFragment.class.getSimpleName());
            }
        }
    }
}