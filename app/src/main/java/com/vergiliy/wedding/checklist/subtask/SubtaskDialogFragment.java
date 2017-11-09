package com.vergiliy.wedding.checklist.subtask;

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

import com.vergiliy.wedding.BaseClass;
import com.vergiliy.wedding.BaseDialogFragment;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.helpers.DecimalDigitsInputFilter;
import com.vergiliy.wedding.helpers.EditTextDatePicker;
import com.vergiliy.wedding.checklist.task.TaskActivity;

import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

import static com.vergiliy.wedding.helpers.BaseHelper.hideKeyboardWhenLostFocus;
import static com.vergiliy.wedding.helpers.EditTextDatePicker.date;

public class SubtaskDialogFragment extends BaseDialogFragment {

    private TaskActivity context;

    private EditText nameField, amountField, dateField;
    private RadioGroup completeField;

    private Subtask subtask;

    // Listener clicks on Edit button
    private class PositiveButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            final String name = nameField.getText().toString();
            final double amount = BaseHelper.parseDouble(amountField.getText().toString(), 0);

            // Get checked item from complete_enable field
            int completeFieldId = completeField.getCheckedRadioButtonId();
            final boolean complete = completeFieldId == R.id.subtask_edit_complete_yes;

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(context, R.string.subtask_dialog_error,
                        Toast.LENGTH_LONG).show();
            } else {
                Subtask item = new Subtask(context);

                item.setIdTask(context.getTask().getId());

                // Update name if it modified
                if (subtask == null || TextUtils.isEmpty(subtask.getLocaleName()) ||
                        !subtask.getLocaleName().equals(name)) {
                    item.setName(BaseClass.LANGUAGE_DEFAULT, name);
                } else {
                    item.setName(BaseClass.LANGUAGE_DEFAULT, subtask.getName());
                }

                item.setAmount(amount);
                item.setDate(date);
                item.setComplete(complete);

                if (subtask != null) {
                    item.setId(subtask.getId());
                    context.getDbSubtask().update(item);
                } else {
                    context.getDbSubtask().add(item);
                }

                if (dialog != null) {
                    dialog.dismiss();
                }

                // Update current fragment
                context.getViewPager().getAdapter().notifyDataSetChanged();
            }
        }
    }

    public static SubtaskDialogFragment newInstance(Subtask subtask) {
        SubtaskDialogFragment fragment = new SubtaskDialogFragment();
        fragment.setSubtask(subtask);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (TaskActivity) super.context;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog_title = inflater.inflate(R.layout.dialog_title, nullGroup);
        View dialog_body = inflater.inflate(R.layout.subtask_diallog_add, nullGroup);

        // Get Title field
        TextView titleView =  dialog_title.findViewById(R.id.dialog_title);

        // Get Body fields
        nameField = dialog_body.findViewById(R.id.subtask_edit_name);
        amountField = dialog_body.findViewById(R.id.subtask_edit_amount);
        completeField = dialog_body.findViewById(R.id.subtask_edit_complete);
        dateField = dialog_body.findViewById(R.id.subtask_edit_date);

        // Get RadioButton
        RadioButton completeFieldYes = completeField.findViewById(R.id.subtask_edit_complete_yes);
        RadioButton completeFieldNo = completeField.findViewById(R.id.subtask_edit_complete_no);

        // Switch Hint to date EditTex
        completeField.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged (RadioGroup group,int id){
                if (id == R.id.subtask_edit_complete_no) {
                    dateField.setHint(R.string.subtask_dialog_date_pending_hint);
                } else {
                    dateField.setHint(R.string.subtask_dialog_date_paid_hint);
                }
            }
        });

        // Get service buttons
        Button cancelButton = dialog_body.findViewById(R.id.dialog_button_cancel);
        Button yesButton = dialog_body.findViewById(R.id.dialog_button_yes);

        // Set filter to amountField (20 digits before zero and 2 digits after zero)
        DecimalFormatSymbols decimal = DecimalFormatSymbols.getInstance(Locale.getDefault());
        amountField.setFilters(new InputFilter[] {new DecimalDigitsInputFilter()});

        if (subtask != null) {
            nameField.setText(subtask.getLocaleName());
            amountField.setText(subtask.getAmountAsString());
            dateField.setText(subtask.getDateAsLocale());

            Boolean complete = subtask.getComplete();
            if (complete) {
                completeFieldYes.setChecked(true);
            } else {
                completeFieldNo.setChecked(true);
                dateField.setHint(R.string.subtask_dialog_date_pending_hint);
            }

            titleView.setText(R.string.subtask_dialog_title_edit);
            yesButton.setText(R.string.dialog_button_edit);
        } else {
            titleView.setText(R.string.subtask_dialog_title_add);
            yesButton.setText(R.string.dialog_button_add);
        }

        // Show DatePicker when click EditText
        Date date = null;
        if (!EditTextDatePicker.isDateSet && subtask != null) {
            date = subtask.getDate();
        }
        dateField.setOnClickListener(new EditTextDatePicker(context, dateField, date));

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

    public void setSubtask(Subtask subtask) {
        this.subtask = subtask;
    }
}