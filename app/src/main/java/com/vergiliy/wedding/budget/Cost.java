package com.vergiliy.wedding.budget;

import android.content.Context;
import android.util.Log;

import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.vendors.BaseClass;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class Cost extends BaseClass {
    private	int	id, id_category;
    private Map<String, String> name = new HashMap<>();
    private	String note = null;
    private	double amount;
    private	boolean	complete;
    private Date update;

    Cost(Context context) {
        super(context);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int getIdCategory() {
        return id_category;
    }

    void setIdCategory(int id_category) {
        this.id_category = id_category;
    }

    String getName() {
        return name.get(LANGUAGE_DEFAULT);
    }

    String getLocaleName() {
        return getLocaleValue(name);
    }

    void setName(String locale, String name) {
        this.name.put(locale, name);
    }

    String getNote() {
        return note;
    }

    void setNote(String note) {
        this.note = note;
    }

    double getAmount() {
        return amount;
    }

    void setAmount(double amount) {
        this.amount = amount;
    }

    boolean getComplete() {
        return complete;
    }

    void setComplete(boolean complete) {
        this.complete = complete;
    }

    Date getUpdate() {
        return update;
    }

    String getUpdateAsString() {
        return BaseHelper.getStringFromDate(update);
    }

    void setUpdate(Date update) {
        this.update = update;
    }
}
