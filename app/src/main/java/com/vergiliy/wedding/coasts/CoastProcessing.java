package com.vergiliy.wedding.coasts;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.vergiliy.wedding.R;
import com.vergiliy.wedding.helpers.BaseHelper;

// Listener clicks on Edit button or FloatingButton (edit or add new coast)
class CoastProcessing implements View.OnClickListener {

    private CoastsActivity context;
    private final ViewGroup nullGroup = null;

    private EditText nameField;
    private EditText quantityField;

    private Coast coast = null;

    // Get coast from main class CoastsRecyclerAdapter
    CoastProcessing(Coast coast) {
        this.coast = coast;
    }

    // Default constructor
    CoastProcessing() {}

    // Listener clicks on Edit button
    private class PositiveButtonListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            final String name = nameField.getText().toString();
            final int quantity = BaseHelper.parseInteger(quantityField.getText().toString(), 0);

            if (TextUtils.isEmpty(name) || quantity <= 0) {
                Toast.makeText(context, R.string.coast_dialog_error,
                        Toast.LENGTH_LONG).show();
            } else {
                if (coast != null) {
                    Coast item = new Coast(coast.getId(), name, quantity);
                    context.getDatabase().update(item);
                } else {
                    Coast item = new Coast(name, quantity);
                    context.getDatabase().add(item);
                }

                // Update current fragment
                context.getViewPager().getAdapter().notifyDataSetChanged();
            }
        }
    }

    // Listener clicks on Cancel button
    private class NegativeButtonListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(context, R.string.coast_dialog_cancel, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        Integer title, label;
        context = getActivity(view);
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.costs_diallog_add, nullGroup);

        // Get EditText fields
        nameField = (EditText) subView.findViewById(R.id.coast_add_name);
        quantityField = (EditText) subView.findViewById(R.id.coast_edit_quantity);

        if (coast != null) {
            nameField.setText(coast.getName());
            quantityField.setText(String.valueOf(coast.getQuantity()));
            title = R.string.coast_dialog_title_edit;
            label = R.string.dialog_button_edit;
        } else {
            title = R.string.coast_dialog_title_add;
            label = R.string.dialog_button_add;
        }

        // Create new alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(subView);
        builder.create();

        // Listener clicks on Edit button
        builder.setPositiveButton(label, new PositiveButtonListener());

        // Listener clicks on Cancel button
        builder.setNegativeButton(R.string.dialog_button_cancel, new NegativeButtonListener());

        // Show dialog
        builder.show();
    }

    // Get main Activity
    private CoastsActivity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof CoastsActivity) {
                return (CoastsActivity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}