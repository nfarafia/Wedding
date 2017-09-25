package com.vergiliy.wedding.setting;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.vergiliy.wedding.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimePreference extends DialogPreference {
    private Integer hour, minute;
    private TimePicker picker = null;

    // Initialization DialogPreference
    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText(R.string.settings_dialog_set);
        setNegativeButtonText(R.string.settings_dialog_cancel);
    }

    // Create dialog TimePicker
    @Override
    protected View onCreateDialogView() {
        Context context = getContext();
        picker = new TimePicker(context);
        boolean is24hour = DateFormat.is24HourFormat(context);
        picker.setIs24HourView(is24hour); // Set 12/24 hour view

        return picker;
    }

    // Build dialog TimePicker
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        if (Build.VERSION.SDK_INT >= 23) {
            picker.setHour(hour);
            picker.setMinute(minute);
        } else {
            picker.setCurrentHour(hour);
            picker.setCurrentMinute(minute);
        }
    }

    // Closing action (save chosen time)
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            if (Build.VERSION.SDK_INT >= 23) {
                hour = picker.getHour();
                minute = picker.getMinute();
            } else {
                hour = picker.getCurrentHour();
                minute = picker.getCurrentMinute();
            }

            // String time = updateTime(hour) + ":" + updateTime(minute);
            String string = String.valueOf(hour) + ":" + String.valueOf(minute);

            if (callChangeListener(string)) {
                persistString(string);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    // Set Initial time
    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String string;

        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentTime = format.format(new Date());

        // If time is given
        if (restoreValue) {
            if (defaultValue == null) {
                string = getPersistedString(currentTime);
            } else {
                string = getPersistedString(defaultValue.toString());
            }
        // Set default (current) time
        } else {
            string = currentTime;
        }

        // Get hour and minute from time
        hour = getHour(string);
        minute = getMinute(string);
    }

    // Get hours from time
    private static Integer getHour(String string) {
        String[] pieces = string.split(":");

        return Integer.parseInt(pieces[0]);
    }

    // Get minutes from time
    private static Integer getMinute(String string) {
        String[] pieces = string.split(":");

        return Integer.parseInt(pieces[1]);
    }

    // Convert time in current system format (12/24)
    String getTimeForSummary(String string) {
        Integer hour, minute;
        String pattern;
        Date date = null;

        boolean is24hour = DateFormat.is24HourFormat(getContext());
        SimpleDateFormat format = new SimpleDateFormat("H:m", Locale.getDefault());

        // Get Date from string
        try {
            date = format.parse(string);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        if (is24hour) {
            pattern = "HH:mm";
        } else {
            pattern = "h:mm a";
        }
        format = new SimpleDateFormat(pattern, Locale.getDefault());

        return format.format(date);
    }
}