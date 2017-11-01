package com.vergiliy.wedding.helpers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.vergiliy.wedding.R;

import java.text.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditTextDatePicker  implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private Context context;
    private EditText dateField;
    private Calendar calendar;
    private boolean isOkayClicked = true;
    private DatePickerDialog dialog;

    public static Date date;
    public static boolean isDateSet = false;

    private class DialogOnClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int id) {
            switch (id) {
                // Set new date
                case DialogInterface.BUTTON_POSITIVE:
                    isOkayClicked = true;
                    DatePicker datePicker = dialog.getDatePicker();
                    onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                    break;
                // Return previous date
                case DialogInterface.BUTTON_NEGATIVE:
                    isOkayClicked = false;
                    break;
                // Clear date
                case DialogInterface.BUTTON_NEUTRAL:
                    isOkayClicked = false;
                    dateField.setText(null);
                    date = null;
                    break;
            }
            dialog.dismiss();
        }
    }

    public EditTextDatePicker(Context context, EditText dateField, Date date) {
        this.context = context;
        this.dateField = dateField;
        calendar = Calendar.getInstance(Locale.getDefault());
        // Set default date
        if (date != null) {
            EditTextDatePicker.isDateSet = true;
            EditTextDatePicker.date = date;
            calendar.setTime(date);
        } else {
            EditTextDatePicker.isDateSet = false;
            EditTextDatePicker.date = null;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (!isOkayClicked) {
            return;
        }

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        // Updates the date in the EditText
        Format format = DateFormat.getDateFormat(context);
        dateField.setText(format.format(calendar.getTime()));

        // Save chosen date
        date = calendar.getTime();
    }

    @Override
    public void onClick(View v) {
        dialog = new DatePickerDialog(context, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                context.getString(R.string.dialog_button_set), new DialogOnClickListener());
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                context.getString(R.string.dialog_button_cancel), new DialogOnClickListener());
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                context.getString(R.string.dialog_button_clear), new DialogOnClickListener());

        dialog.show();
    }
}