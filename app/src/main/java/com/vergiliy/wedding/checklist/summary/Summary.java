package com.vergiliy.wedding.checklist.summary;

import android.content.Context;

import com.vergiliy.wedding.BaseClass;

public class Summary extends BaseClass {
    private	double amount, paid, pending;
    private	int tasksTotal, subtasksTotal, subtasksPaid;

    public Summary(Context context) {
        super(context);
    }

    double getAmount() {
        return amount;
    }

    String getAmountAsString() {
        return BaseClass.getDoubleAsString(getAmount());
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    double getPaid() {
        return paid;
    }

    String getPaidAsString() {
        return BaseClass.getDoubleAsString(getPaid());
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    double getPending() {
        return pending;
    }

    String getPendingAsString() {
        return BaseClass.getDoubleAsString(getPending());
    }

    public void setPending(double pending) {
        this.pending = pending;
    }

    double getBalance() {
        return getAmount() - getPaid() - getPending();
    }

    String getBalanceAsString() {
        double balance = getBalance();
        return (balance > 0 ? "+"  : "") + BaseClass.getDoubleAsString(balance);
    }

    public void setTasksTotal(int count) {
        this.tasksTotal = count;
    }

    private Integer getTasksTotal() {
        return tasksTotal;
    }

    String getTasksTotalAsString() {
        return getTasksTotal().toString();
    }

    public void setSubtasksTotal(int count) {
        this.subtasksTotal = count;
    }

    private Integer getSubtasksTotal() {
        return subtasksTotal;
    }

    String getSubtasksTotalAsString() {
        return getSubtasksTotal().toString();
    }

    public void setSubtasksPaid(int count) {
        this.subtasksPaid = count;
    }

    private Integer getSubtasksPaid() {
        return subtasksPaid;
    }

    String getSubtasksPaidAsString() {
        return getSubtasksPaid().toString();
    }

    private Integer getSubtasksPending() {
        return getSubtasksTotal() - getSubtasksPaid();
    }

    String getSubtasksPendingAsString() {
        return getSubtasksPending().toString();
    }
}
