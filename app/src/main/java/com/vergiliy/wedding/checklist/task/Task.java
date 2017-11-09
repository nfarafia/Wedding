package com.vergiliy.wedding.checklist.task;

import android.content.Context;

import com.vergiliy.wedding.BaseClass;

import java.util.HashMap;
import java.util.Map;

public class Task extends BaseClass {
    private	int	id_category;
    private Map<String, String> name = new HashMap<>();
    private Map<String, String> note = new HashMap<>();
    private	double amount, paid, pending;

    Task(Context context) {
        super(context);
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

    public String getLocaleName() {
        return getLocaleValue(name);
    }

    void setName(String locale, String name) {
        this.name.put(locale, name);
    }

    String getNote() {
        return note.get(LANGUAGE_DEFAULT);
    }

    String getLocaleNote() {
        return getLocaleValue(note);
    }

    // Return locale note with default string
    String getLocaleNote(int resource) {
        return getStringWithDefault(getLocaleNote(), resource);
    }

    void setNote(String locale, String note) {
        this.note.put(locale, note);
    }

    double getAmount() {
        return amount;
    }

    public String getAmountAsString() {
        return BaseClass.getDoubleAsString(getAmount());
    }

    void setAmount(double amount) {
        this.amount = amount;
    }

    private double getPaid() {
        return paid;
    }

    public String getPaidAsString() {
        return BaseClass.getDoubleAsString(getPaid());
    }

    void setPaid(double paid) {
        this.paid = paid;
    }

    private double getPending() {
        return pending;
    }

    public String getPendingAsString() {
        return BaseClass.getDoubleAsString(getPending());
    }

    void setPending(double pending) {
        this.pending = pending;
    }

    double getBalance() {
        return getAmount() - getPaid() - getPending();
    }

    String getBalanceAsString() {
        double balance = getBalance();
        return (balance > 0 ? "+"  : "") + BaseClass.getDoubleAsString(balance);
    }
}
