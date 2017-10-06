package com.vergiliy.wedding.budget;

import com.vergiliy.wedding.helpers.BaseHelper;

import java.util.Date;

class Coast {
    private	int	id, id_category;
    private	String name;
    private	String note;
    private	double	amount;
    private	boolean	complete;
    private Date update;

    Coast(int id_category, String name, String note, double amount, boolean complete) {
        this.id_category = id_category;
        this.name = name;
        this.note = note;
        this.amount = amount;
        this.complete = complete;
        this.update = BaseHelper.getCurrentDate(); // Get current date
    }

    Coast(int id, int id_category, String name, String note, double amount, boolean complete) {
        this.id = id;
        this.id_category = id_category;
        this.name = name;
        this.note = note;
        this.amount = amount;
        this.complete = complete;
        this.update = BaseHelper.getCurrentDate(); // Get current date
    }

    Coast(int id, int id_category, String name, String note, double amount, boolean complete,
          Date update) {
        this.id = id;
        this.id_category = id_category;
        this.name = name;
        this.note = note;
        this.amount = amount;
        this.complete = complete;
        this.update = update;
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

    public void setIdCategory(int id) {
        this.id = id_category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    boolean getComplete() {
        return complete;
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

    public void setUpdate(Date update) {
        this.update = update;
    }
}
