package com.vergiliy.wedding.budget.payment;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.BaseListener;

import static com.vergiliy.wedding.budget.payment.PaymentsRecyclerAdapter.actionMode;

// Creating Payment Dialog
public class PaymentDialogListener extends BaseListener implements View.OnClickListener {

    private Payment payment = null;

    // Get payment from main class BudgetRecyclerAdapter
    PaymentDialogListener(Payment payment) {
        this.payment = payment;
    }

    // Default constructor
    public PaymentDialogListener() {}

    @Override
    public void onClick(View view) {
        BaseActivity activity = getActivity(view);

        // Close ActionMode if it was open
        if (actionMode != null) {
            actionMode.finish();
        }

        if (activity != null) {
            FragmentManager manager = activity.getSupportFragmentManager();
            PaymentDialogFragment fragment = (PaymentDialogFragment) manager
                            .findFragmentByTag(PaymentDialogFragment.class.getSimpleName());
            if (fragment == null) {
                fragment = PaymentDialogFragment.newInstance(payment);
                fragment.show(manager, PaymentDialogFragment.class.getSimpleName());
            }
        }
    }
}