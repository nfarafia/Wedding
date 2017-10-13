package com.vergiliy.wedding.budget.cost;

import android.content.Context;

import com.vergiliy.wedding.BaseClass;

import java.util.HashMap;
import java.util.Map;

public class Cost extends BaseClass {
    private	int	id_category;
    private Map<String, String> name = new HashMap<>();
    private Map<String, String> note = new HashMap<>();
    private	double amount;

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

    String getAmountAsString() {
        return super.getAmountAsString(getAmount());
    }

    void setAmount(double amount) {
        this.amount = amount;
    }
}
