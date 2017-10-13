package com.vergiliy.wedding.budget.cost;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vergiliy.wedding.budget.payment.Payment;
import com.vergiliy.wedding.budget.payment.PaymentDatabase;
import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.helpers.SQLiteHelper;
import com.vergiliy.wedding.BaseClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CostDatabase extends SQLiteHelper {

    private static final String TABLE = "budget_costs";
    private static final String COLUMN_ID_CATEGORY = "id_category";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_AMOUNT = "amount";

    // Create access to database
    public CostDatabase(Context context) {
        super(context);
    }

    // Get cost by id
    Cost getOne(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM %s WHERE _id = %d",  TABLE, id);

        Cost cost = null;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            cost = getCostByCursor(cursor);
        }

        cursor.close();
        return cost;
    }

    // Get all fields
    /*
    private List<Payment> getAll(){
        return getAll(null);
    }
    */

    // Get all fields
    private List<Cost> getAll(Integer id_category){
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sql = new StringBuilder();

        sql.append(String.format(Locale.getDefault(),
                "SELECT c.*, SUM(CASE WHEN complete > 0 THEN p.amount ELSE 0 END) paid, " +
                        "SUM(CASE WHEN complete == 0 THEN p.amount ELSE 0 END) pending " +
                        "FROM %s c " +
                        "LEFT JOIN %s p ON (c._id = p.id_cost) ",
                TABLE, PaymentDatabase.TABLE));

        if (id_category > 0) {
            sql.append(String.format(Locale.getDefault(), "WHERE id_category = %d ", id_category));
        }

        sql.append("GROUP BY c._id ORDER BY _id ASC");

        List<Cost> all = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                Cost cost = getCostByCursor(cursor);
                cost.setPaid(cursor.getDouble(10));
                cost.setPending(cursor.getDouble(11));
                all.add(cost);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return all;
    }

    // Get all fields by category id
    public List<Cost> getAllByIdCategory(int id_category){
        return getAll(id_category);
    }

    // Add new field
    void add(Cost cost){
        cost.setUpdate(BaseHelper.getCurrentDate()); // Get current date
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE, null, getValues(cost));
    }

    // Update field
    public void update(Cost cost){
        cost.setUpdate(BaseHelper.getCurrentDate()); // Get current date
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE, getValues(cost), COLUMN_ID	+ "	= ?",
                new String[] { String.valueOf(cost.getId())});
    }

    // Delete field
    public void delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
    }

    private Cost getCostByCursor(Cursor cursor) {
        Cost cost = new Cost(context);
        cost.setId(Integer.parseInt(cursor.getString(0)));
        cost.setIdCategory(Integer.parseInt(cursor.getString(1)));
        cost.setName(BaseClass.LANGUAGE_DEFAULT, cursor.getString(2));
        cost.setName(BaseClass.LANGUAGE_EN, cursor.getString(3));
        cost.setName(BaseClass.LANGUAGE_RU, cursor.getString(4));
        cost.setNote(BaseClass.LANGUAGE_DEFAULT, cursor.getString(5));
        cost.setNote(BaseClass.LANGUAGE_EN, cursor.getString(6));
        cost.setNote(BaseClass.LANGUAGE_RU, cursor.getString(7));
        cost.setAmount(cursor.getDouble(8));
        cost.setUpdate(BaseHelper.getDateFromString(cursor.getString(9)));
        return cost;
    }

    private ContentValues getValues(Cost cost){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CATEGORY, cost.getIdCategory());
        values.put(COLUMN_NAME, cost.getName());
        values.put(COLUMN_NOTE, cost.getNote());
        values.put(COLUMN_AMOUNT, cost.getAmount());
        values.put(COLUMN_UPDATE, cost.getUpdateAsString());
        return values;
    }
}
