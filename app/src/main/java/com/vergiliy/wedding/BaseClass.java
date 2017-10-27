package com.vergiliy.wedding;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;

import com.vergiliy.wedding.helpers.BaseHelper;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public class BaseClass {
    protected Context context;

    protected int id;
    protected Date update;

    public final static String LANGUAGE_DEFAULT = "default";
    public final static String LANGUAGE_EN = "en";
    public final static String LANGUAGE_RU = "ru";

    protected BaseClass(Context context) {
        this.context = context;
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

    protected String getDateAsLocale(Date date) {
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
            String language;

            if (context instanceof BaseActivity) {
                language = ((BaseActivity) context).getLanguage();
            // Get current language, if main class is not BaseActivity
            } else {
                Language languageClass = new Language(context);
                language = languageClass.getLanguage();
            }

            String localeValue = map.get(language);
            String defaultValue = map.get(LANGUAGE_EN);
            if (!isEmpty(localeValue)) {
                return localeValue;
            } else if (!isEmpty(defaultValue)) {
                return defaultValue;
            }
        }

        return userValue;
    }

    // Find index (and return it) in List<Object>
    public static <T> Integer findIndexInListById(List<T> all, int id) {
        for (int i = 0; i < all.size(); i++) {
            if (((BaseClass) all.get(i)).getId() == id) {
                return i;
            }
        }
        return null;
    }

    // Find Object (and return it) in List<Object>
    public static <T> T findObjectInListById(List<T> all, int id) {
        Integer index = findIndexInListById(all, id);
        if (index != null) {
            return all.get(index);
        }
        return null;
    }
}
