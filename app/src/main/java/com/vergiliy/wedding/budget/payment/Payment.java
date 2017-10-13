package com.vergiliy.wedding.budget.payment;

import android.content.Context;

import com.vergiliy.wedding.BaseClass;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Payment extends BaseClass {
    private	int	id_cost;
    private Map<String, String> name = new HashMap<>();
    private	double amount;
    private	boolean	complete;
    private Date date;

    Payment(Context context) {
        super(context);
    }

    int getIdCost() {
        return id_cost;
    }

    void setIdCost(int id_cost) {
        this.id_cost = id_cost;
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
        return super.getDoubleAsString(getAmount());
    }

    void setAmount(double amount) {
        this.amount = amount;
    }

    Date getDate() {
        return date;
    }

    String getDateAsString() {
        return super.getDateAsString(date);
    }

    String getDateAsLocale(int resource) {
        return super.getDateAsLocale(date, resource);
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
