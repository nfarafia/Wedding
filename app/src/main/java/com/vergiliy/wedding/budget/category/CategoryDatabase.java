package com.vergiliy.wedding.budget.category;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.helpers.SQLiteHelper;
import com.vergiliy.wedding.BaseClass;

import java.util.ArrayList;
import java.util.List;

public class CategoryDatabase extends SQLiteHelper {

    private	static final String TABLE = "budget_categories";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_POSITION = "position";

    // Create access to database
    public CategoryDatabase(Context context) {
        super(context);
    }

    // Get all fields
    public List<Category> getAll(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE + " ORDER BY " +
                "CASE WHEN position IS NOT NULL THEN 0 ELSE 1 END ASC, position ASC, _id ASC";
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

    // Add new field
    void add(Category category){
        category.setUpdate(BaseHelper.getCurrentDate()); // Get current date
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE, null, getValues(category));
    }

    // Update field
    public void update(Category category){
        category.setUpdate(BaseHelper.getCurrentDate()); // Get current date
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE, getValues(category), COLUMN_ID	+ "	= ?",
                new String[] {String.valueOf(category.getId())});
    }

    // Delete field
    void delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
    }
    
    private Category getCategoryByCursor(Cursor cursor) {
        Category category = new Category(context);
        category.setId(Integer.parseInt(cursor.getString(0)));
        category.setName(BaseClass.LANGUAGE_DEFAULT, cursor.getString(1));
        category.setName(BaseClass.LANGUAGE_EN, cursor.getString(2));
        category.setName(BaseClass.LANGUAGE_RU, cursor.getString(3));
        category.setPosition(Integer.parseInt(cursor.getString(4)));
        category.setUpdate(BaseHelper.getDateFromString(cursor.getString(5)));
        return category;
    }

    private ContentValues getValues(Category category){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, category.getName());
        values.put(COLUMN_POSITION, category.getPosition());
        values.put(COLUMN_UPDATE, category.getUpdateAsString());
        return values;
    }
}
