package com.vergiliy.wedding.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import static com.vergiliy.wedding.helpers.BaseHelper.readFromAssets;

// SQLite helper (main class for access to db_main)
public class SQLiteHelper extends SQLiteOpenHelper {

    private	static final int VERSION =	1;
    private	static final String	DATABASE_NAME = "wedding";
    protected Context context;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    // Create table from appropriate file when it does not exist
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Get SQL query from file
        String sql = readFromAssets(context, "database_create.sql");

        // Execution multiple SQL queries
        execMultipleSQL(db, sql);
    }

    // Update table when version change
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Get SQL query from file
        String sql = readFromAssets(context, "database_delete.sql");

        // Execution multiple SQL queries
        execMultipleSQL(db, sql);

        // Recreate tablets
        onCreate(db);
    }

    // Execution multiple SQL queries
    private void execMultipleSQL(SQLiteDatabase db, String sql) {
        try {
            if (sql != null) {
                String[] queries = sql.split(";");
                for (String query : queries) {
                    if (!TextUtils.isEmpty(query)) {
                        // Log.e("Query", query);
                        db.execSQL(query);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Exception", "CoastsSectionsDatabase -> execMultipleSQL: " + e.getMessage());
        }
    }
}
