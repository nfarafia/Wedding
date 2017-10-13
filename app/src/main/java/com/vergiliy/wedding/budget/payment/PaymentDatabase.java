package com.vergiliy.wedding.budget.payment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.helpers.SQLiteHelper;
import com.vergiliy.wedding.BaseClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PaymentDatabase extends SQLiteHelper {

    private	static final String TABLE = "budget_payments";
    private static final String COLUMN_ID_COST = "id_cost";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "`date`";
    private static final String COLUMN_COMPLETE = "complete";

    // Create access to database
    public PaymentDatabase(Context context) {
        super(context);
    }

    // Get all fields
    /*
    private List<Payment> getAll(){
        return getAll(null);
    }
    */

    // Get all fields
    private List<Payment> getAll(Integer id_cost){
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLE);

        if (id_cost > 0) {
            sql.append(String.format(Locale.getDefault(), " WHERE id_cost = %d",  id_cost));
        }

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

    // Add new field
    void add(Payment payment){
        payment.setUpdate(BaseHelper.getCurrentDate()); // Get current date
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE, null, getValues(payment));
    }

    // Update field
    public void update(Payment payment){
        payment.setUpdate(BaseHelper.getCurrentDate()); // Get current date
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE, getValues(payment), COLUMN_ID	+ "	= ?",
                new String[] { String.valueOf(payment.getId())});
    }

    // Delete field
    void delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
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
        payment.setUpdate(BaseHelper.getDateFromString(cursor.getString(8)));
        return payment;
    }

    private ContentValues getValues(Payment payment){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_COST, payment.getIdCost());
        values.put(COLUMN_NAME, payment.getName());
        values.put(COLUMN_AMOUNT, payment.getAmount());
        values.put(COLUMN_DATE, payment.getDateAsString());
        values.put(COLUMN_COMPLETE, payment.getComplete());
        values.put(COLUMN_UPDATE, payment.getUpdateAsString());
        return values;
    }
}
