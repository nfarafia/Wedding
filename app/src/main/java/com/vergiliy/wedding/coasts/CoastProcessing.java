package com.vergiliy.wedding.coasts;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vergiliy.wedding.R;
import com.vergiliy.wedding.helpers.BaseHelper;

import java.util.List;

// Listener clicks on Edit button or FloatingButton (edit or add new coast)
class CoastProcessing implements View.OnClickListener {

    private CoastsActivity context;
    private final ViewGroup nullGroup = null;

    private Spinner sectionField;
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
            final int id_section = ((CoastsSection) sectionField.getSelectedItem()).getId();
            final int quantity = BaseHelper.parseInteger(quantityField.getText().toString(), 0);

            if (TextUtils.isEmpty(name) || quantity <= 0) {
                Toast.makeText(context, R.string.coast_dialog_error,
                        Toast.LENGTH_LONG).show();
            } else {
                if (coast != null) {
                    Coast item = new Coast(coast.getId(), id_section, name, quantity);
                    context.getDbMain().update(item);
                } else {
                    Coast item = new Coast(id_section, name, quantity);
                    context.getDbMain().add(item);
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
        sectionField = (Spinner) subView.findViewById(R.id.coast_edit_section);
        nameField = (EditText) subView.findViewById(R.id.coast_edit_name);
        quantityField = (EditText) subView.findViewById(R.id.coast_edit_quantity);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CoastsSection> adapter = new ArrayAdapter<>(context,
                R.layout.spinner_item, context.getSections());

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionField.setAdapter(adapter);
        // Chose the current section
        sectionField.setSelection(context.getViewPager().getCurrentItem());

        if (coast != null) {
            nameField.setText(coast.getName());
            quantityField.setText(String.valueOf(coast.getQuantity()));
            title = R.string.coast_dialog_title_edit;
            label = R.string.dialog_button_edit;
        } else {
            title = R.string.coast_dialog_title_add;
            label = R.string.dialog_button_add;
        }

        // Create custom title
        TextView titleBox = new TextView(context);
        titleBox.setTextColor(ContextCompat.getColor(context, R.color.text));
        titleBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        titleBox.setPadding(30, 45, 30, 30);
        titleBox.setText(title);

        // Create new alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogFullScreen);
        builder.setCustomTitle(titleBox);
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