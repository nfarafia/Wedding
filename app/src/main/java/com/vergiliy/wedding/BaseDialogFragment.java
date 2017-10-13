package com.vergiliy.wedding;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vergiliy.wedding.budget.cost.CostActivity;

public class BaseDialogFragment extends DialogFragment {

    protected FragmentActivity context;

    protected AlertDialog dialog;

    protected final ViewGroup nullGroup = null;

    // Listener clicks on Cancel button
    public class NegativeButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Toast.makeText(context, R.string.dialog_cancel, Toast.LENGTH_LONG).show();
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        // Save user date when screen rotate
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        // Workaround for this issue: https://code.google.com/p/android/issues/detail?id=17423
        // (unable to retain instance after configuration change)
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }
}
