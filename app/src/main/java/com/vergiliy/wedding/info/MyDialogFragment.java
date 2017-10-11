package com.vergiliy.wedding.info;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.R;

public class MyDialogFragment extends DialogFragment {

    private final ViewGroup nullGroup = null;

    private EditText nameField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BaseActivity context = (BaseActivity) getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog_body = inflater.inflate(R.layout.cost_diallog_add, nullGroup);

        // Get Body fields
        nameField = dialog_body.findViewById(R.id.cost_edit_name);

        // Create new alert dialog
        return new AlertDialog.Builder(context, R.style.DialogFullScreen)
                .setTitle("Title")
                .setView(dialog_body)
                .create();
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