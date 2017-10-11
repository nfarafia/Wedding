package com.vergiliy.wedding.vendors;

import android.content.Context;
import android.text.TextUtils;

import com.vergiliy.wedding.BaseActivity;

import java.util.Map;

public class BaseClass {
    protected BaseActivity context;

    public final static String LANGUAGE_DEFAULT = "default";
    public final static String LANGUAGE_EN = "en";
    public final static String LANGUAGE_RU = "ru";

    protected BaseClass(Context context) {
        this.context = (BaseActivity) context;
    }

    protected String getLocaleValue(Map<String, String> map) {
        String userValue = map.get(LANGUAGE_DEFAULT);

        if (TextUtils.isEmpty(userValue)) {
            String localeValue = map.get(context.getLanguage());
            String defaultValue = map.get(LANGUAGE_EN);
            if (!TextUtils.isEmpty(localeValue)) {
                return localeValue;
            } else if (!TextUtils.isEmpty(defaultValue)) {
                return defaultValue;
            }
        }

        return userValue;
    }
}
