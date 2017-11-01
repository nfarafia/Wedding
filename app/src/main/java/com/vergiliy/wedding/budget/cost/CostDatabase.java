package com.vergiliy.wedding.budget.cost;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vergiliy.wedding.budget.balance.Balance;
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

    @Override
    public String getTable() {
        return TABLE;
    }

    @Override
    protected ContentValues getValues(BaseClass item) {
        Cost cost = (Cost) item;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CATEGORY, cost.getIdCategory());
        values.put(COLUMN_NAME, cost.getName());
        values.put(COLUMN_NOTE, cost.getNote());
        values.put(COLUMN_AMOUNT, cost.getAmount());
        return addDefaultContentValues(values, item);
    }

    // Get cost by id
    Cost getOne(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = String.format(Locale.getDefault(),
                "SELECT c.*, SUM(CASE WHEN p.complete > 0 THEN p.amount ELSE 0 END) paid, " +
                        "SUM(CASE WHEN p.complete == 0 THEN p.amount ELSE 0 END) pending " +
                        "FROM %s c " +
                        "LEFT JOIN %s p ON (p.active = 1 AND c._id = p.id_cost) " +
                        "WHERE c.active = 1 AND c._id = %d",
                TABLE, PaymentDatabase.TABLE, id);

        Cost cost = null;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            cost = getCostByCursor(cursor);
        }

        cursor.close();
        return cost;
    }

    // Get all fields
    private List<Cost> getAll(Integer id_category){
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sql = new StringBuilder();

        sql.append(String.format(Locale.getDefault(),
                "SELECT c.*, SUM(CASE WHEN p.complete > 0 THEN p.amount ELSE 0 END) paid, " +
                        "SUM(CASE WHEN p.complete == 0 THEN p.amount ELSE 0 END) pending " +
                        "FROM %s c " +
                        "LEFT JOIN %s p ON (p.active = 1 AND c._id = p.id_cost) " +
                        "WHERE c.active = 1 ",
                TABLE, PaymentDatabase.TABLE));

        if (id_category != null && id_category > 0) {
            sql.append(String.format(Locale.getDefault(), "AND c.id_category = %d ", id_category));
        }

        sql.append("GROUP BY c._id ORDER BY _id ASC");

        List<Cost> all = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                Cost cost = getCostByCursor(cursor);
                all.add(cost);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return all;
    }

    // Get balance by Category
    public Balance getBalance(Integer id_category){
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sql = new StringBuilder();

        sql.append(String.format(Locale.getDefault(),
                "SELECT SUM(g.amount) amount, SUM(g.paid) paid, SUM(g.pending) pending, " +
                        "SUM(g.costs) costs, SUM(g.payments) payments, " +
                        "SUM(g.payments_complete) payments_complete " +
                        "FROM (SELECT c.amount, " +
                        "SUM(CASE WHEN p.complete > 0 THEN p.amount ELSE 0 END) paid, " +
                        "SUM(CASE WHEN p.complete == 0 THEN p.amount ELSE 0 END) pending, " +
                        "COUNT(DISTINCT c._id) costs, COUNT(DISTINCT p._id) payments, " +
                        "SUM(CASE WHEN p.complete > 0 THEN 1 ELSE 0 END) payments_complete " +
                        "FROM %s c " +
                        "LEFT JOIN %s p ON (p.active = 1 AND c._id = p.id_cost) " +
                        "WHERE c.active = 1 ",
                TABLE, PaymentDatabase.TABLE));

        if (id_category != null && id_category > 0) {
            sql.append(String.format(Locale.getDefault(), "AND c.id_category = %d ", id_category));
        }

        sql.append("GROUP BY c._id) g");

        Balance balance = new Balance(context);
        Cursor cursor = db.rawQuery(sql.toString(), null);

        if (cursor.moveToFirst()) {
            balance.setAmount(cursor.getDouble(0));
            balance.setPaid(cursor.getDouble(1));
            balance.setPending(cursor.getDouble(2));
            balance.setCoatsTotal(cursor.getInt(3));
            balance.setPaymentsTotal(cursor.getInt(4));
            balance.setPaymentsPaid(cursor.getInt(5));
        }

        cursor.close();
        return balance;
    }

    // Get all fields by category id
    public List<Cost> getAllByIdCategory(int id_category){
        return getAll(id_category);
    }

    // Get all fields
    private int getCount(Integer id_category){
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sql = new StringBuilder();

        sql.append(String.format(Locale.getDefault(),
                "SELECT COUNT(_id) FROM %s WHERE active = 1 ", TABLE));

        if (id_category != null && id_category > 0) {
            sql.append(String.format(Locale.getDefault(), "AND id_category = %d ", id_category));
        }

        Cursor cursor = db.rawQuery(sql.toString(), null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        cursor.close();

        return 0;
    }

    // Get count fields by category id
    public int getCountByIdCategory(int id_category){
        return getCount(id_category);
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
        cost.setPaid(cursor.getDouble(11));
        cost.setPending(cursor.getDouble(12));
        return cost;
    }
}
