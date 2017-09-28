package com.vergiliy.wedding.coasts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vergiliy.wedding.helpers.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CoastsDatabase extends SQLiteHelper {

    private	static final String TABLE = "coasts";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_IDSECTION = "id_section";
    private static final String COLUMN_PRODUCTNAME = "productname";
    private static final String COLUMN_QUANTITY = "quantity";

    // Create access to database
    CoastsDatabase(Context context) {
        super(context);
    }

    // Get all fields
    private List<Coast> getAll(){
        return getAll(null);
    }

    // Get all fields
    private List<Coast> getAll(Integer section_id){
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLE);
        if (section_id > 0) {
            sql.append(String.format(Locale.getDefault(), " WHERE id_section = %d",  section_id));
        }

        List<Coast> all = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                int id_section = Integer.parseInt(cursor.getString(1));
                String name = cursor.getString(2);
                int quantity = Integer.parseInt(cursor.getString(3));
                all.add(new Coast(id, id_section, name, quantity));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return all;
    }

    // Get all fields by section id
    List<Coast> getAllBySectionId(int section_id){
        return getAll(section_id);
    }

    // Add new field
    void add(Coast product){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IDSECTION, product.getIdSection());
        values.put(COLUMN_PRODUCTNAME, product.getName());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        db.insert(TABLE, null, values);
    }

    // Update field
    void update(Coast product){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IDSECTION, product.getIdSection());
        values.put(COLUMN_PRODUCTNAME, product.getName());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        db.update(TABLE, values, COLUMN_ID	+ "	= ?",
                new String[] { String.valueOf(product.getId())});
    }

    /*
    // Find fiend
    Coast find(String name){
        SQLiteDatabase db = getWritableDatabase();
        String query = "Select * FROM "	+ TABLE + " WHERE name=" + COLUMN_PRODUCTNAME;
        Coast coast = null;
        Cursor cursor = db.rawQuery(query,	null);

        if	(cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(0));
            String productName = cursor.getString(1);
            int productQuantity = Integer.parseInt(cursor.getString(2));
            coast = new Coast(id, productName, productQuantity);
        }

        cursor.close();
        return coast;
    }
    */

    // Delete field
    void delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
    }
}
