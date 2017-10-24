package com.vergiliy.wedding.budget.cost;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vergiliy.wedding.BaseDialogFragment;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.budget.BudgetInterface;
import com.vergiliy.wedding.budget.category.Category;
import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.helpers.DecimalDigitsInputFilter;
import com.vergiliy.wedding.BaseClass;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static com.vergiliy.wedding.helpers.BaseHelper.hideKeyboardWhenLostFocus;

public class CostDialogFragment extends BaseDialogFragment {

    private Cost cost;

    private EditText nameField;
    private Spinner categoryField;
    private EditText noteField;
    private EditText amountField;

    // Listener clicks on Edit button
    private class PositiveButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            final String name = nameField.getText().toString();
            final int id_category = ((Category) categoryField.getSelectedItem()).getId();
            final String note = noteField.getText().toString();
            final double amount = BaseHelper.parseDouble(amountField.getText().toString(), 0);

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(context, R.string.cost_dialog_error,
                        Toast.LENGTH_LONG).show();
            } else {
                Cost item = new Cost(context);

                item.setIdCategory(id_category);

                // Update name if it modified
                if (cost == null || TextUtils.isEmpty(cost.getLocaleName()) ||
                        !cost.getLocaleName().equals(name)) {
                    item.setName(BaseClass.LANGUAGE_DEFAULT, name);

                    // Update CostActivity Title
                    if (context instanceof CostActivity ) {
                        context.setTitle(item.getLocaleName());
                    }
                } else {
                    item.setName(BaseClass.LANGUAGE_DEFAULT, cost.getName());
                }

                // Update note if it modified
                if (!TextUtils.isEmpty(note)) {
                    if (cost == null || TextUtils.isEmpty(cost.getLocaleNote()) ||
                            !cost.getLocaleNote().equals(note)) {
                        item.setNote(BaseClass.LANGUAGE_DEFAULT, note);
                    } else {
                        item.setNote(BaseClass.LANGUAGE_DEFAULT, cost.getNote());
                    }
                }

                item.setAmount(amount);

                if (cost != null) {
                    item.setId(cost.getId());
                    ((BudgetInterface) context).getDbCost().update(item);
                } else {
                    ((BudgetInterface) context).getDbCost().add(item);
                }

                if (dialog != null) {
                    dialog.dismiss();
                }

                // Update Cost
                if (context instanceof CostActivity ) {
                    cost = ((BudgetInterface) context).getDbCost().getOne(cost.getId());
                }

                // Update current fragment
                ((BudgetInterface) context).getViewPager().getAdapter().notifyDataSetChanged();
            }
        }
    }

    public static CostDialogFragment newInstance(Cost cost) {
        CostDialogFragment fragment = new CostDialogFragment();
        fragment.setCost(cost);
        return fragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog_title = inflater.inflate(R.layout.dialog_title, nullGroup);
        View dialog_body = inflater.inflate(R.layout.cost_diallog_add, nullGroup);

        // Get Title field
        TextView titleView =  dialog_title.findViewById(R.id.dialog_title);

        // Get Body fields
        nameField = dialog_body.findViewById(R.id.cost_edit_name);
        categoryField = dialog_body.findViewById(R.id.cost_edit_category);
        noteField = dialog_body.findViewById(R.id.cost_edit_note);
        amountField = dialog_body.findViewById(R.id.cost_edit_amount);

        // Get service buttons
        Button cancelButton = dialog_body.findViewById(R.id.dialog_button_cancel);
        Button yesButton = dialog_body.findViewById(R.id.dialog_button_yes);

        // Set filter to amountField (20 digits before zero and 2 digits after zero)
        DecimalFormatSymbols decimal = DecimalFormatSymbols.getInstance(Locale.getDefault());
        amountField.setFilters(new InputFilter[] {new DecimalDigitsInputFilter()});

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(context,
                R.layout.spinner_item, ((BudgetInterface) context).getCategories());

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryField.setAdapter(adapter);

        // Chose the current category
        categoryField.setSelection(((BudgetInterface) context).getViewPager().getCurrentItem());

        if (cost != null) {
            nameField.setText(cost.getLocaleName());
            noteField.setText(cost.getLocaleNote());
            amountField.setText(cost.getAmountAsString());
            categoryField.setSelection(cost.getIdCategory() - 1);
            titleView.setText(R.string.cost_dialog_title_edit);
            yesButton.setText(R.string.dialog_button_edit);
        } else {
            titleView.setText(R.string.cost_dialog_title_add);
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

        return dialog;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }
}