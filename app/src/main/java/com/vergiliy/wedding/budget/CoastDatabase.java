package com.vergiliy.wedding.budget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vergiliy.wedding.helpers.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CoastDatabase extends SQLiteHelper {

    private	static final String TABLE = "budget_coasts";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ID_CATEGORY = "id_category";
    private static final String COLUMN_PRODUCTNAME = "productname";
    private static final String COLUMN_QUANTITY = "quantity";

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
                int quantity = Integer.parseInt(cursor.getString(3));
                all.add(new Coast(id, id_category, name, quantity));
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
        values.put(COLUMN_PRODUCTNAME, coast.getName());
        values.put(COLUMN_QUANTITY, coast.getQuantity());
        db.insert(TABLE, null, values);
    }

    // Update field
    void update(Coast coast){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CATEGORY, coast.getIdCategory());
        values.put(COLUMN_PRODUCTNAME, coast.getName());
        values.put(COLUMN_QUANTITY, coast.getQuantity());
        db.update(TABLE, values, COLUMN_ID	+ "	= ?",
                new String[] { String.valueOf(coast.getId())});
    }

    // Delete field
    void delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
    }
}
