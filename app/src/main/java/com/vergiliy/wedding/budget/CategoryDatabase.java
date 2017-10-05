package com.vergiliy.wedding.budget;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vergiliy.wedding.helpers.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

class CategoryDatabase extends SQLiteHelper {

    private	static final String TABLE = "budget_categories";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_POSITION = "position";

    // Create access to database
    CategoryDatabase(Context context) {
        super(context);
    }

    // Get all fields
    List<Category> getAll(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE + " ORDER BY " + COLUMN_POSITION + " ASC";
        List<Category> all = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                all.add(new Category(id, name));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return all;
    }
}
