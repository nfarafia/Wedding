package com.vergiliy.wedding.budget.cost;

import android.content.Context;
import android.text.TextUtils;

import com.vergiliy.wedding.R;
import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.vendors.BaseClass;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Cost extends BaseClass {
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

    public String getLocaleName() {
        return getLocaleValue(name);
    }

    void setName(String locale, String name) {
        this.name.put(locale, name);
    }

    String getNote() {
        return note;
    }

    // Return note with default string
    String getNote(int resource) {
        String note = getNote();
        return TextUtils.isEmpty(note) ? context.getString(resource) : note;
    }

    void setNote(String note) {
        this.note = note;
    }

    double getAmount() {
        return amount;
    }

    String getAmountAsString() {
        Double amount = getAmount();
        String format = amount % 1 == 0 ? "%.0f" : "%.2f";
        return String.format(Locale.getDefault(), format, amount);
    }

    void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean getComplete() {
        return complete;
    }

    String getCompleteAsString() {
        int resource = getComplete() ?
                R.string.cost_view_complete_enable : R.string.cost_view_complete_disable;
        return context.getString(resource);
    }

    public void setComplete(boolean complete) {
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
