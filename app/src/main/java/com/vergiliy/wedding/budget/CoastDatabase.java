package com.vergiliy.wedding.budget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.helpers.SQLiteHelper;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.vergiliy.wedding.helpers.BaseHelper.getDateFromString;

class CoastDatabase extends SQLiteHelper {

    private	static final String TABLE = "budget_coasts";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ID_CATEGORY = "id_category";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_COMPLETE = "complete";
    private static final String COLUMN_UPDATE = "`update`";

    // Create access to database
    CoastDatabase(Context context) {
        super(context);
    }

    // Get all fields
    private List<Coast> getAll(){
        return getAll(null);
    }

    // Get all fields
    private List<Coast> getAll(Integer category_id){
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLE);
        if (category_id > 0) {
            sql.append(String.format(Locale.getDefault(), " WHERE id_category = %d",  category_id));
        }

        List<Coast> all = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                int id_category = Integer.parseInt(cursor.getString(1));
                String name = cursor.getString(2);
                String note = cursor.getString(3);
                double amount = cursor.getDouble(4);
                boolean complete = Integer.parseInt(cursor.getString(5)) > 0;
                Date update = BaseHelper.getDateFromString(cursor.getString(6));
                all.add(new Coast(id, id_category, name, note, amount, complete, update));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return all;
    }

    // Get all fields by category id
    List<Coast> getAllByCategoryId(int id_category){
        return getAll(id_category);
    }

    // Add new field
    void add(Coast coast){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CATEGORY, coast.getIdCategory());
        values.put(COLUMN_NAME, coast.getName());
        values.put(COLUMN_NOTE, coast.getNote());
        values.put(COLUMN_AMOUNT, coast.getAmount());
        values.put(COLUMN_COMPLETE, coast.getComplete());
        values.put(COLUMN_UPDATE, coast.getUpdateAsString());
        db.insert(TABLE, null, values);
    }

    // Update field
    void update(Coast coast){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CATEGORY, coast.getIdCategory());
        values.put(COLUMN_NAME, coast.getName());
        values.put(COLUMN_NOTE, coast.getNote());
        values.put(COLUMN_AMOUNT, coast.getAmount());
        values.put(COLUMN_COMPLETE, coast.getComplete());
        values.put(COLUMN_UPDATE, coast.getUpdateAsString());
        db.update(TABLE, values, COLUMN_ID	+ "	= ?",
                new String[] { String.valueOf(coast.getId())});
    }

    // Delete field
    void delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
    }
}
