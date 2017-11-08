package com.vergiliy.wedding.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.vergiliy.wedding.BaseClass;

import static android.R.attr.type;
import static com.vergiliy.wedding.helpers.BaseHelper.readFromAssets;

// SQLite helper (main class for access to db_cost, db_payment etc.)
public abstract class SQLiteHelper extends SQLiteOpenHelper {

    private	static final int VERSION = 1;
    private	static final String	DATABASE_NAME = "wedding";
    protected static final String COLUMN_ID = "_id";
    private static final String COLUMN_UPDATE = "`update`";
    private static final String COLUMN_ACTIVE = "active";
    protected Context context;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    public abstract String getTable();

    // Create table from appropriate file when it does not exist
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables for budget and fill them data
        restore(db);
    }

    // Update table when version change
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete tables if exist
        execMultipleSQL(db, readFromAssets(context, "database_delete.sql"));

        // Recreate tablets
        onCreate(db);
    }

    // Add new field
    public void add(BaseClass item){
        item.setUpdate(BaseHelper.getCurrentDate()); // Set current date
        SQLiteDatabase db = getWritableDatabase();
        db.insert(getTable(), null, getValues(item));
    }

    // Update field
    public void update(BaseClass item){
        item.setUpdate(BaseHelper.getCurrentDate()); // Set current date
        SQLiteDatabase db = getWritableDatabase();
        db.update(getTable(), getValues(item), COLUMN_ID + " = ?",
                new String[] {String.valueOf(item.getId())});
    }

    // Delete field
    public void delete(BaseClass item){
        item.setUpdate(BaseHelper.getCurrentDate()); // Set current date
        item.setActive(false); // Mark as deleted
        SQLiteDatabase db = getWritableDatabase();
        db.update(getTable(), getValues(item), COLUMN_ID + " = ?",
                new String[] {String.valueOf(item.getId())});
    }

    // Delete all field
    public void deleteAll() {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_UPDATE, BaseHelper.getStringFromDate(BaseHelper.getCurrentDate()));
        values.put(COLUMN_ACTIVE, false);
        db.update(getTable(), values, COLUMN_ACTIVE + "	> 0", null);
    }

    protected abstract ContentValues getValues(BaseClass item);

    protected ContentValues addDefaultContentValues(ContentValues values, BaseClass item) {
        values.put(COLUMN_UPDATE, item.getUpdateAsString());
        values.put(COLUMN_ACTIVE, item.getActive());
        return values;
    }

    // Execution multiple SQL queries
    private void execMultipleSQL(SQLiteDatabase db, String sql) {
        try {
            if (sql != null) {
                String[] queries = sql.split(";");
                for (String query : queries) {
                    if (!TextUtils.isEmpty(query)) {
                        db.execSQL(query);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Exception", "CostsSectionsDatabase -> execMultipleSQL: " + e.getMessage());
        }
    }

    // Restore budget tables from onCreate
    private void restore(SQLiteDatabase db) {
        execMultipleSQL(db, readFromAssets(context, "database_create.sql"));
    }

    // Restore budget tables
    public void restore() {
        SQLiteDatabase db = getWritableDatabase();
        restore(db);
    }
}
