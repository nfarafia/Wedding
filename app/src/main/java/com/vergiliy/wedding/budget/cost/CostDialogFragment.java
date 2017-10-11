package com.vergiliy.wedding.budget.cost;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
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
import com.vergiliy.wedding.budget.BudgetInterface;
import com.vergiliy.wedding.budget.category.Category;
import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.helpers.DecimalDigitsInputFilter;
import com.vergiliy.wedding.vendors.BaseClass;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static com.vergiliy.wedding.helpers.BaseHelper.hideKeyboardWhenLostFocus;

public class CostDialogFragment extends DialogFragment {

    private FragmentActivity context;
    AlertDialog dialog;
    private final ViewGroup nullGroup = null;

    private EditText nameField;
    private Spinner categoryField;
    private EditText noteField;
    private EditText amountField;
    private RadioGroup completeField;

    private Cost cost;

    // Listener clicks on Edit button
    private class PositiveButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            final String name = nameField.getText().toString();
            final int id_category = ((Category) categoryField.getSelectedItem()).getId();
            final String note = noteField.getText().toString();
            final double amount = BaseHelper
                    .parseDouble(amountField.getText().toString().replace(',','.'), 0);

            // Get checked item from complete_enable field
            int completeFieldId = completeField.getCheckedRadioButtonId();
            final boolean complete = completeFieldId == R.id.cost_edit_complete_yes;

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

                if (!TextUtils.isEmpty(note))
                    item.setNote(note);

                item.setAmount(amount);
                item.setComplete(complete);

                if (cost != null) {
                    item.setId(cost.getId());
                    ((BudgetInterface) context).getDbMain().update(item);
                } else {
                    ((BudgetInterface) context).getDbMain().add(item);
                }

                if (dialog != null) {
                    dialog.dismiss();
                }

                // Update Cost
                if (context instanceof CostActivity ) {
                    cost = ((BudgetInterface) context).getDbMain().getOne(cost.getId());
                }

                // Update current fragment
                ((BudgetInterface) context).getViewPager().getAdapter().notifyDataSetChanged();
            }
        }
    }

    // Listener clicks on Cancel button
    private class NegativeButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Toast.makeText(context, R.string.cost_dialog_cancel, Toast.LENGTH_LONG).show();
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    public static CostDialogFragment newInstance(Cost cost) {
        CostDialogFragment fragment = new CostDialogFragment();
        fragment.setCost(cost);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        // Save user date when screen rotate
        setRetainInstance(true);
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
        completeField = dialog_body.findViewById(R.id.cost_edit_complete);

        // Get RadioButton
        RadioButton completeFieldYes = completeField.findViewById(R.id.cost_edit_complete_yes);
        RadioButton completeFieldNo = completeField.findViewById(R.id.cost_edit_complete_no);

        // Get service buttons
        Button cancelButton = dialog_body.findViewById(R.id.dialog_button_cancel);
        Button yesButton = dialog_body.findViewById(R.id.dialog_button_yes);

        // Set filter to amountField (20 digits before zero and 2 digits after zero)
        DecimalFormatSymbols decimal = DecimalFormatSymbols.getInstance(Locale.getDefault());
        amountField.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(20,2)});

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
            noteField.setText(cost.getNote());
            amountField.setText(cost.getAmountAsString());

            Boolean complete = cost.getComplete();
            if (complete) {
                completeFieldYes.setChecked(true);
            } else {
                completeFieldNo.setChecked(true);
            }

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

    @Override
    public void onDestroyView() {
        // Workaround for this issue: https://code.google.com/p/android/issues/detail?id=17423
        // (unable to retain instance after configuration change)
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }
}