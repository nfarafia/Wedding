package com.vergiliy.wedding.budget.category;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.BaseClass;
import com.vergiliy.wedding.helpers.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryDatabase extends SQLiteHelper {

    private	static final String TABLE = "budget_categories";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_POSITION = "position";

    // Create access to database
    public CategoryDatabase(Context context) {
        super(context);
    }

    @Override
    public String getTable() {
        return TABLE;
    }

    @Override
    protected ContentValues getValues(BaseClass item) {
        Category category = (Category) item;
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, category.getName());
        values.put(COLUMN_POSITION, category.getPosition());
        return addDefaultContentValues(values, item);
    }

    // Get all fields
    public List<Category> getAll(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM %s WHERE active = 1 " +
                        "ORDER BY CASE WHEN position IS NOT NULL THEN 0 ELSE 1 END ASC, " +
                        "position ASC, _id ASC", TABLE);
        List<Category> all = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                all.add(getCategoryByCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return all;
    }
    
    private Category getCategoryByCursor(Cursor cursor) {
        Category category = new Category(context);
        category.setId(Integer.parseInt(cursor.getString(0)));
        category.setName(BaseClass.LANGUAGE_DEFAULT, cursor.getString(1));
        category.setName(BaseClass.LANGUAGE_EN, cursor.getString(2));
        category.setName(BaseClass.LANGUAGE_RU, cursor.getString(3));
        String position = cursor.getString(4);
        category.setPosition(position == null ? null : Integer.parseInt(position));
        category.setUpdate(BaseHelper.getDateFromString(cursor.getString(5)));
        return category;
    }
}
