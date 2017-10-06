package com.vergiliy.wedding.budget;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vergiliy.wedding.R;
import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.helpers.DecimalDigitsInputFilter;
import com.vergiliy.wedding.vendors.BaseClass;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static com.vergiliy.wedding.helpers.BaseHelper.hideKeyboardWhenLostFocus;

// Listener clicks on Edit button or FloatingButton (edit or add new coast)
class CoastProcessing implements View.OnClickListener {

    private BudgetActivity context;
    private AlertDialog dialog = null;
    private final ViewGroup nullGroup = null;

    private EditText nameField;
    private Spinner categoryField;
    private EditText noteField;
    private EditText amountField;
    private RadioGroup completeField;

    private Coast coast = null;

    // Get coast from main class BudgetRecyclerAdapter
    CoastProcessing(Coast coast) {
        this.coast = coast;
    }

    // Default constructor
    CoastProcessing() {}

    // Listener clicks on Edit button
    private class PositiveButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            final String name = nameField.getText().toString();
            final int id_category = ((Category) categoryField.getSelectedItem()).getId();
            final String note = noteField.getText().toString();
            final double amount = BaseHelper
                    .parseDouble(amountField.getText().toString().replace(',','.'), 0);

            // Get checked item from complete field
            int completeFieldId = completeField.getCheckedRadioButtonId();
            final boolean complete = completeFieldId == R.id.coast_edit_complete_yes;

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(context, R.string.coast_dialog_error,
                        Toast.LENGTH_LONG).show();
            } else {
                Coast item = new Coast(context);

                item.setIdCategory(id_category);

                // Update name if it modified
                if (coast == null || !coast.getLocaleName().equals(name))
                    item.setName(BaseClass.LANGUAGE_DEFAULT, name);

                if (!TextUtils.isEmpty(note))
                    item.setNote(note);

                item.setAmount(amount);
                item.setComplete(complete);
                item.setUpdate(BaseHelper.getCurrentDate()); // Get current date

                if (coast != null) {
                    item.setId(coast.getId());
                    context.getDbMain().update(item);
                } else {
                    context.getDbMain().add(item);
                }

                if (dialog != null) {
                    dialog.dismiss();
                }

                // Update current fragment
                context.getViewPager().getAdapter().notifyDataSetChanged();
            }
        }
    }

    // Listener clicks on Cancel button
    private class NegativeButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Toast.makeText(context, R.string.coast_dialog_cancel, Toast.LENGTH_LONG).show();
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onClick(View view) {
        context = getActivity(view);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog_title = inflater.inflate(R.layout.dialog_title, nullGroup);
        View dialog_body = inflater.inflate(R.layout.cost_diallog_add, nullGroup);

        // Get Title field
        TextView titleView =  dialog_title.findViewById(R.id.dialog_title);

        // Get Body fields
        nameField = dialog_body.findViewById(R.id.coast_edit_name);
        categoryField = dialog_body.findViewById(R.id.coast_edit_category);
        noteField = dialog_body.findViewById(R.id.coast_edit_note);
        amountField = dialog_body.findViewById(R.id.coast_edit_amount);
        completeField = dialog_body.findViewById(R.id.coast_edit_complete);

        // Get RadioButton
        RadioButton completeFieldYes = completeField.findViewById(R.id.coast_edit_complete_yes);
        RadioButton completeFieldNo = completeField.findViewById(R.id.coast_edit_complete_no);

        // Get service buttons
        Button cancelButton = dialog_body.findViewById(R.id.dialog_button_cancel);
        Button yesButton = dialog_body.findViewById(R.id.dialog_button_yes);

        // Set filter to amountField (20 digits before zero and 2 digits after zero)
        DecimalFormatSymbols decimal = DecimalFormatSymbols.getInstance(Locale.getDefault());
        amountField.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(20,2)});

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(context,
                R.layout.spinner_item, context.getCategories());

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryField.setAdapter(adapter);
        // Chose the current category
        categoryField.setSelection(context.getViewPager().getCurrentItem());

        if (coast != null) {
            nameField.setText(coast.getLocaleName());
            noteField.setText(coast.getNote());

            Double amount = coast.getAmount();
            String format = amount % 1 == 0 ? "%.0f" : "%.2f";
            amountField.setText(String.format(Locale.getDefault(), format, coast.getAmount()));

            Boolean complete = coast.getComplete();
            if (complete) {
                completeFieldYes.setChecked(true);
            } else {
                completeFieldNo.setChecked(true);
            }

            titleView.setText(R.string.coast_dialog_title_edit);
            yesButton.setText(R.string.dialog_button_edit);
        } else {
            titleView.setText(R.string.coast_dialog_title_add);
            yesButton.setText(R.string.dialog_button_add);
        }

        // Create new alert dialog
        dialog = new AlertDialog.Builder(context, R.style.DialogFullScreen)
                .setCustomTitle(dialog_title)
                .setView(dialog_body)
                .create();

        // Don't close dialog, when click outside
        dialog.setCanceledOnTouchOutside(false);

        // Hide keyboard after clicking outside
        hideKeyboardWhenLostFocus(context, dialog_body);
        hideKeyboardWhenLostFocus(context, dialog_title);

        yesButton.setOnClickListener(new PositiveButtonListener());
        cancelButton.setOnClickListener(new NegativeButtonListener());

        // Show dialog
        dialog.show();
    }

    // Get main Activity
    private BudgetActivity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof BudgetActivity) {
                return (BudgetActivity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}