package com.vergiliy.wedding.coasts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vergiliy.wedding.helpers.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

class CoastsSectionsDatabase extends SQLiteHelper {

    private	static final String TABLE = "coasts_sections";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_POSITION = "position";

    // Create access to database
    CoastsSectionsDatabase(Context context) {
        super(context);
    }

    // Get all fields
    List<CoastsSection> getAll(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE + " ORDER BY " + COLUMN_POSITION + " ASC";
        List<CoastsSection> all = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                all.add(new CoastsSection(id, name));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return all;
    }
}
