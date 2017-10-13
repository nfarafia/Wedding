package com.vergiliy.wedding;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.helpers.BaseHelper;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static android.R.attr.resource;
import static android.text.TextUtils.isEmpty;
import static com.vergiliy.wedding.setting.DatePreference.DATA_FORMAT;

public class BaseClass {
    protected BaseActivity context;

    protected int id;
    protected Date update;

    public final static String LANGUAGE_DEFAULT = "default";
    public final static String LANGUAGE_EN = "en";
    public final static String LANGUAGE_RU = "ru";

    protected BaseClass(Context context) {
        this.context = (BaseActivity) context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getUpdate() {
        return update;
    }

    public String getUpdateAsString() {
        return getDateAsString(update);
    }

    private String getUpdateDateAsLocale() {
        return getDateAsLocale(update);
    }

    private String getUpdateTimeAsLocale() {
        return getTimeAsLocale(update);
    }

    public String getUpdateAsLocale(int resource) {
        String date = getUpdateDateAsLocale();
        if (TextUtils.isEmpty(date)) {
            return context.getString(resource);
        } else {
            String time = getUpdateTimeAsLocale();
            return context.getString(R.string.date_time, date, time);
        }
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    protected String getDoubleAsString(Double amount) {
        String format = amount % 1 == 0 ? "%.0f" : "%.2f";
        return String.format(Locale.getDefault(), format, amount);
    }

    protected String getDateAsString(Date date) {
        return BaseHelper.getStringFromDate(date);
    }

    private String getDateAsLocale(Date date) {
        Format format = DateFormat.getDateFormat(context); // Get system date format
        return date != null ? format.format(date): null;
    }

    protected String getDateAsLocale(Date date, int resource) {
        return getStringWithDefault(getDateAsLocale(date), resource);
    }

    // Convert time in current system format (12/24)
    private String getTimeAsLocale(Date date) {
        String pattern;

        boolean is24hour = DateFormat.is24HourFormat(context);
        if (is24hour) {
            pattern = "HH:mm";
        } else {
            pattern = "h:mm a";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());

        return format.format(date);
    }

    protected String getStringWithDefault(String string, int defaultResource) {
        return TextUtils.isEmpty(string) ? context.getString(defaultResource) : string;
    }

    protected String getLocaleValue(Map<String, String> map) {
        String userValue = map.get(LANGUAGE_DEFAULT);

        if (isEmpty(userValue)) {
            String localeValue = map.get(context.getLanguage());
            String defaultValue = map.get(LANGUAGE_EN);
            if (!isEmpty(localeValue)) {
                return localeValue;
            } else if (!isEmpty(defaultValue)) {
                return defaultValue;
            }
        }

        return userValue;
    }
}
