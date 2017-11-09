package com.vergiliy.wedding.checklist.subtask;

import android.content.Context;

import com.vergiliy.wedding.BaseClass;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Subtask extends BaseClass {
    private	int id_task;
    private Map<String, String> name = new HashMap<>();
    private	double amount;
    private	boolean	complete;
    private Date date;

    Subtask(Context context) {
        super(context);
    }

    int getIdTask() {
        return id_task;
    }

    void setIdTask(int id_task) {
        this.id_task = id_task;
    }

    String getName() {
        return name.get(LANGUAGE_DEFAULT);
    }

    public String getLocaleName() {
        return getLocaleValue(name);
    }

    void setName(String locale, String name) {
        this.name.put(locale, name);
    }

    double getAmount() {
        return amount;
    }

    String getAmountAsString() {
        return BaseClass.getDoubleAsString(getAmount());
    }

    void setAmount(double amount) {
        this.amount = amount;
    }

    Date getDate() {
        return date;
    }

    String getDateAsString() {
        return super.getDateAsString(getDate());
    }

    String getDateAsLocale() {
        return super.getDateAsLocale(getDate());
    }

    private Date getDateWithComplete() {
        return (complete && date == null) ? update : date;
    }

    String getDateAsLocaleWithComplete(int resource) {
        return super.getDateAsLocale(getDateWithComplete(), resource);
    }

    void setDate(Date date) {
        this.date = date;
    }

    boolean getComplete() {
        return complete;
    }

    void setComplete(boolean complete) {
        this.complete = complete;
    }
}
