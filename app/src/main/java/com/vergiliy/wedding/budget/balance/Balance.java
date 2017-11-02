package com.vergiliy.wedding.budget.balance;

import android.content.Context;

import com.vergiliy.wedding.BaseClass;

public class Balance extends BaseClass {
    private	double amount, paid, pending;
    private	int costsTotal, paymentsTotal, paymentsPaid;

    public Balance(Context context) {
        super(context);
    }

    double getAmount() {
        return amount;
    }

    String getAmountAsString() {
        return super.getDoubleAsString(getAmount());
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    double getPaid() {
        return paid;
    }

    String getPaidAsString() {
        return super.getDoubleAsString(getPaid());
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    double getPending() {
        return pending;
    }

    String getPendingAsString() {
        return super.getDoubleAsString(getPending());
    }

    public void setPending(double pending) {
        this.pending = pending;
    }

    double getBalance() {
        return getAmount() - getPaid() - getPending();
    }

    String getBalanceAsString() {
        double balance = getBalance();
        return (balance > 0 ? "+"  : "") + super.getDoubleAsString(balance);
    }

    public void setCoatsTotal(int count) {
        this.costsTotal = count;
    }

    private Integer getCoatsTotal() {
        return costsTotal;
    }

    String getCoatsTotalAsString() {
        return getCoatsTotal().toString();
    }

    public void setPaymentsTotal(int count) {
        this.paymentsTotal = count;
    }

    private Integer getPaymentsTotal() {
        return paymentsTotal;
    }

    String getPaymentsTotalAsString() {
        return getPaymentsTotal().toString();
    }

    public void setPaymentsPaid(int count) {
        this.paymentsPaid = count;
    }

    private Integer getPaymentsPaid() {
        return paymentsPaid;
    }

    String getPaymentsPaidAsString() {
        return getPaymentsPaid().toString();
    }

    private Integer getPaymentsPending() {
        return getPaymentsTotal() - getPaymentsPaid();
    }

    String getPaymentsPendingAsString() {
        return getPaymentsPending().toString();
    }
}
