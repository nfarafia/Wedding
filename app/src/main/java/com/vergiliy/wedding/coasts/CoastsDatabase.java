package com.vergiliy.wedding.coasts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vergiliy.wedding.helpers.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

class CoastsDatabase extends SQLiteHelper {

    private	static final int VERSION =	1;
    private	static final String TABLE = "coasts";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PRODUCTNAME = "productname";
    private static final String COLUMN_QUANTITY = "quantity";

    // Create access to database
    CoastsDatabase(Context context) {
        super(context, VERSION);
    }

    // Create table when it does not exist
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE =
                "CREATE	TABLE " + TABLE +
                        "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                        COLUMN_PRODUCTNAME + " TEXT," +
                        COLUMN_QUANTITY + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    // Update table when version change
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // Get all fields
    List<Coast> getAll(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE;
        List<Coast> all = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                int quantity = Integer.parseInt(cursor.getString(2));
                all.add(new Coast(id, name, quantity));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return all;
    }

    // Add new field
    public void add(Coast product){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getName());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        db.insert(TABLE, null, values);
    }

    // Update field
    void update(Coast product){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getName());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        db.update(TABLE, values, COLUMN_ID	+ "	= ?",
                new String[] { String.valueOf(product.getId())});
    }

    /*
    // Find fiend
    public Coast find(String name){
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
