package com.vergiliy.wedding.budget.category;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vergiliy.wedding.helpers.SQLiteHelper;
import com.vergiliy.wedding.vendors.BaseClass;

import java.util.ArrayList;
import java.util.List;

public class CategoryDatabase extends SQLiteHelper {

    private	static final String TABLE = "budget_categories";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_POSITION = "position";
    private static final String COLUMN_UPDATE = "`update`";

    // Create access to database
    public CategoryDatabase(Context context) {
        super(context);
    }

    // Get all fields
    public List<Category> getAll(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE + " ORDER BY " + COLUMN_POSITION + " ASC";
        List<Category> all = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(context);

                category.setId(Integer.parseInt(cursor.getString(0)));
                category.setName(BaseClass.LANGUAGE_DEFAULT, cursor.getString(1));
                category.setName(BaseClass.LANGUAGE_EN, cursor.getString(2));
                category.setName(BaseClass.LANGUAGE_RU, cursor.getString(3));

                all.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return all;
    }
}
