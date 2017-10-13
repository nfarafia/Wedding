package com.vergiliy.wedding.budget.payment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vergiliy.wedding.BaseDialogFragment;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.budget.cost.CostActivity;
import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.helpers.DecimalDigitsInputFilter;
import com.vergiliy.wedding.BaseClass;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static com.vergiliy.wedding.helpers.BaseHelper.hideKeyboardWhenLostFocus;

public class PaymentDialogFragment extends BaseDialogFragment {

    private CostActivity context;

    private Payment payment;

    private EditText nameField;
    private EditText amountField;
    private RadioGroup completeField;

    // Listener clicks on Edit button
    private class PositiveButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            final String name = nameField.getText().toString();
            final double amount = BaseHelper.parseDouble(amountField.getText().toString(), 0);

            // Get checked item from complete_enable field
            int completeFieldId = completeField.getCheckedRadioButtonId();
            final boolean complete = completeFieldId == R.id.payment_edit_complete_yes;

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(context, R.string.payment_dialog_error,
                        Toast.LENGTH_LONG).show();
            } else {
                Payment item = new Payment(context);

                item.setIdCost(context.getCost().getId());

                // Update name if it modified
                if (payment == null || TextUtils.isEmpty(payment.getLocaleName()) ||
                        !payment.getLocaleName().equals(name)) {
                    item.setName(BaseClass.LANGUAGE_DEFAULT, name);
                } else {
                    item.setName(BaseClass.LANGUAGE_DEFAULT, payment.getName());
                }

                item.setAmount(amount);
                item.setComplete(complete);

                if (payment != null) {
                    item.setId(payment.getId());
                    context.getDbPayment().update(item);
                } else {
                    context.getDbPayment().add(item);
                }

                if (dialog != null) {
                    dialog.dismiss();
                }

                // Update current fragment
                context.getViewPager().getAdapter().notifyDataSetChanged();
            }
        }
    }

    public static PaymentDialogFragment newInstance(Payment payment) {
        PaymentDialogFragment fragment = new PaymentDialogFragment();
        fragment.setPayment(payment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (CostActivity) super.context;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog_title = inflater.inflate(R.layout.dialog_title, nullGroup);
        View dialog_body = inflater.inflate(R.layout.payment_diallog_add, nullGroup);

        // Get Title field
        TextView titleView =  dialog_title.findViewById(R.id.dialog_title);

        // Get Body fields
        nameField = dialog_body.findViewById(R.id.payment_edit_name);
        amountField = dialog_body.findViewById(R.id.payment_edit_amount);
        completeField = dialog_body.findViewById(R.id.payment_edit_complete);

        // Get RadioButton
        RadioButton completeFieldYes = completeField.findViewById(R.id.payment_edit_complete_yes);
        RadioButton completeFieldNo = completeField.findViewById(R.id.payment_edit_complete_no);

        // Get service buttons
        Button cancelButton = dialog_body.findViewById(R.id.dialog_button_cancel);
        Button yesButton = dialog_body.findViewById(R.id.dialog_button_yes);

        // Set filter to amountField (20 digits before zero and 2 digits after zero)
        DecimalFormatSymbols decimal = DecimalFormatSymbols.getInstance(Locale.getDefault());
        amountField.setFilters(new InputFilter[] {new DecimalDigitsInputFilter()});

        if (payment != null) {
            nameField.setText(payment.getLocaleName());
            amountField.setText(payment.getAmountAsString());

            Boolean complete = payment.getComplete();
            if (complete) {
                completeFieldYes.setChecked(true);
            } else {
                completeFieldNo.setChecked(true);
            }

            titleView.setText(R.string.payment_dialog_title_edit);
            yesButton.setText(R.string.dialog_button_edit);
        } else {
            titleView.setText(R.string.payment_dialog_title_add);
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

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}