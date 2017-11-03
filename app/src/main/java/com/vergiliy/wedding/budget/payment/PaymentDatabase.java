package com.vergiliy.wedding.budget.payment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.helpers.SQLiteHelper;
import com.vergiliy.wedding.BaseClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PaymentDatabase extends SQLiteHelper {

    public static final String TABLE = "budget_payments";
    private static final String COLUMN_ID_COST = "id_cost";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_COMPLETE = "complete";
    private static final String COLUMN_NOTIFICATION = "notification";

    // Create access to database
    public PaymentDatabase(Context context) {
        super(context);
    }

    @Override
    public String getTable() {
        return TABLE;
    }

    @Override
    protected ContentValues getValues(BaseClass item) {
        Payment payment = (Payment) item;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_COST, payment.getIdCost());
        values.put(COLUMN_NAME, payment.getName());
        values.put(COLUMN_AMOUNT, payment.getAmount());
        values.put(COLUMN_DATE, payment.getDateAsString());
        values.put(COLUMN_COMPLETE, payment.getComplete());
        return addDefaultContentValues(values, item);
    }

    // Get all fields
    private List<Payment> getAll(Integer id_cost){
        SQLiteDatabase db = getReadableDatabase();

        StringBuilder sql =
                new StringBuilder(String.format(Locale.getDefault(),
                        "SELECT * FROM %s WHERE active = 1 ", TABLE));

        if (id_cost != null &&  id_cost > 0) {
            sql.append(String.format(Locale.getDefault(), "AND id_cost = %d ", id_cost));
        }

        sql.append("ORDER BY complete DESC, CASE WHEN date IS NOT NULL THEN 0 ELSE 1 END ASC, " +
                "DATE(date) ASC, _id ASC");

        List<Payment> all = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                all.add(getPaymentByCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return all;
    }

    // Get all fields by cost id
    public List<Payment> getAllByIdCost(int id_cost){
        return getAll(id_cost);
    }

    // Get fields for notification
    public List<Payment> getAllForNotification(){
        SQLiteDatabase db = getReadableDatabase();

        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM %s WHERE active = 1 AND complete = 0 " +
                        "AND (notification IS NULL || DATE(notification) < DATE(DATETIME('now'))) " +
                        "AND DATE(date) <= DATE(DATETIME('now'))",
                TABLE);

        List<Payment> all = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                all.add(getPaymentByCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return all;
    }

    // Mark field as notified
    public void setNotification(String[] id){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFICATION, BaseHelper.getStringFromDate(BaseHelper.getCurrentDate()));
        SQLiteDatabase db = getReadableDatabase();
        db.update(TABLE, values, COLUMN_ID
                + "	IN (" + TextUtils.join(",", Collections.nCopies(id.length, "?"))  + ")", id);
    }

    private Payment getPaymentByCursor(Cursor cursor) {
        Payment payment = new Payment(context);
        payment.setId(Integer.parseInt(cursor.getString(0)));
        payment.setIdCost(Integer.parseInt(cursor.getString(1)));
        payment.setName(BaseClass.LANGUAGE_DEFAULT, cursor.getString(2));
        payment.setName(BaseClass.LANGUAGE_EN, cursor.getString(3));
        payment.setName(BaseClass.LANGUAGE_RU, cursor.getString(4));
        payment.setAmount(cursor.getDouble(5));
        payment.setDate(BaseHelper.getDateFromString(cursor.getString(6)));
        payment.setComplete(Integer.parseInt(cursor.getString(7)) > 0);
        payment.setUpdate(BaseHelper.getDateFromString(cursor.getString(9)));
        return payment;
    }
}
