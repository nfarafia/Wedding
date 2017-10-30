package com.vergiliy.wedding.budget.category;

import android.content.Context;

import com.vergiliy.wedding.BaseClass;

import java.util.HashMap;
import java.util.Map;

public class Category extends BaseClass {

    private	int	id, position;
    private Map<String, String> name = new HashMap<>();

    Category(Context context) {
        super(context);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name.get(LANGUAGE_DEFAULT);
    }

    public String getLocaleName() {
        return getLocaleValue(name);
    }

    void setName(String locale, String name) {
        this.name.put(locale, name);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return getLocaleName();
    }
}