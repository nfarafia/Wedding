package com.vergiliy.wedding.setting;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import com.vergiliy.wedding.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatePreference extends DialogPreference {
    private Integer date, month, year;
    private DatePicker picker = null;
    public static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());

    // Initialization DialogPreference
    public DatePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText(R.string.settings_dialog_set);
        setNegativeButtonText(R.string.settings_dialog_cancel);
    }

    // Create dialog TimePicker
    @Override
    protected View onCreateDialogView() {
        picker = new DatePicker(getContext());

        // setCalendarViewShown(false) attribute is only available from API level 11
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            picker.setCalendarViewShown(false);
        }

        return picker;
    }

    // Build dialog DatePicker
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        picker.updateDate(year, month - 1, date);
    }

    // Closing action (save chosen date)
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            year = picker.getYear();
            month = picker.getMonth() + 1;
            date = picker.getDayOfMonth();

            String string = String.valueOf(year)
                    + "-" + String.valueOf(month)
                    + "-" + String.valueOf(date);

            if (callChangeListener(string)) {
                persistString(string);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    // Set Initial time
    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String string;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = format.format(new Date());

        // If date is given
        if (restoreValue) {
            if (defaultValue == null) {
                string = getPersistedString(currentDate);
            } else {
                string = getPersistedString(defaultValue.toString());
            }
        // Set default (current) date
        } else {
            string = currentDate;
        }

        // Get year, month and minute from date
        year = getYear(string);
        month = getMonth(string);
        date = getDate(string);
    }

    // Get year from date string
    private static int getYear(String string) {
        String[] pieces = string.split("-");

        return Integer.parseInt(pieces[0]);
    }

    // Get month from date string
    private static int getMonth(String string) {
        String[] pieces = string.split("-");

        return Integer.parseInt(pieces[1]);
    }

    // Get date from date string
    private static int getDate(String string) {
        String[] pieces = string.split("-");

        return Integer.parseInt(pieces[2]);
    }

    // Get date for summary
    String getDateForSummary(String string) {
        Date date = null;

        // Get Date from string
        try {
            date = DATA_FORMAT.parse(string);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Format format = DateFormat.getDateFormat(getContext());

        return format.format(date);
    }
}
