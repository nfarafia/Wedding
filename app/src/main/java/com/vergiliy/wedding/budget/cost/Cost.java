package com.vergiliy.wedding.budget.cost;

import android.content.Context;

import com.vergiliy.wedding.BaseClass;

import java.util.HashMap;
import java.util.Map;

public class Cost extends BaseClass {
    private	int	id_category;
    private Map<String, String> name = new HashMap<>();
    private Map<String, String> note = new HashMap<>();
    private	double amount, paid, pending;

    Cost(Context context) {
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

    // Return localу note with default string
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
        return super.getDoubleAsString(getAmount());
    }

    void setAmount(double amount) {
        this.amount = amount;
    }

    private double getPaid() {
        return paid;
    }

    public String getPaidAsString() {
        return super.getDoubleAsString(getPaid());
    }

    void setPaid(double paid) {
        this.paid = paid;
    }

    private double getPending() {
        return pending;
    }

    public String getPendingAsString() {
        return super.getDoubleAsString(getPending());
    }

    void setPending(double pending) {
        this.pending = pending;
    }

    double getBalance() {
        return getAmount() - getPaid() - getPending();
    }

    String getBalanceAsString() {
        double balance = getBalance();
        return (balance > 0 ? "+"  : "") + super.getDoubleAsString(balance);
    }
}
