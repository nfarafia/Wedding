package com.vergiliy.wedding.checklist.subtask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.vergiliy.wedding.BaseClass;
import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.helpers.SQLiteHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SubtaskDatabase extends SQLiteHelper {

    public static final String TABLE = "checklist_subtasks";
    private static final String COLUMN_ID_TASK = "id_task";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_COMPLETE = "complete";
    private static final String COLUMN_NOTIFICATION = "notification";

    // Create access to database
    public SubtaskDatabase(Context context) {
        super(context);
    }

    @Override
    public String getTable() {
        return TABLE;
    }

    @Override
    protected ContentValues getValues(BaseClass item) {
        Subtask subtask = (Subtask) item;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_TASK, subtask.getIdTask());
        values.put(COLUMN_NAME, subtask.getName());
        values.put(COLUMN_AMOUNT, subtask.getAmount());
        values.put(COLUMN_DATE, subtask.getDateAsString());
        values.put(COLUMN_COMPLETE, subtask.getComplete());
        return addDefaultContentValues(values, item);
    }

    // Get all fields
    private List<Subtask> getAll(Integer id_task){
        SQLiteDatabase db = getReadableDatabase();

        StringBuilder sql =
                new StringBuilder(String.format(Locale.getDefault(),
                        "SELECT * FROM %s WHERE active = 1 ", TABLE));

        if (id_task != null &&  id_task > 0) {
            sql.append(String.format(Locale.getDefault(), "AND id_task = %d ", id_task));
        }

        sql.append("ORDER BY complete DESC, CASE WHEN date IS NOT NULL THEN 0 ELSE 1 END ASC, " +
                "DATE(date) ASC, _id ASC");

        List<Subtask> all = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                all.add(getSubtaskByCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return all;
    }

    // Get all fields by task id
    public List<Subtask> getAllByIdTask(int id_task){
        return getAll(id_task);
    }

    // Get fields for notification
    public List<Subtask> getAllForNotification(){
        SQLiteDatabase db = getReadableDatabase();

        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM %s WHERE active = 1 AND complete = 0 " +
                        "AND (notification IS NULL || " +
                        "DATE(notification) < DATE(DATETIME('now','localtime'))) " +
                        "AND DATE(date) <= DATE(DATETIME('now', 'localtime'))",
                TABLE);

        List<Subtask> all = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                all.add(getSubtaskByCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return all;
    }

    // Mark field as notified
    public void setNotification(String[] id){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFICATION, BaseHelper.getStringFromDate(BaseHelper.getCurrentDate()));
        SQLiteDatabase db = getReadableDatabase();
        db.update(TABLE, values, COLUMN_ID
                + "	IN (" + TextUtils.join(",", Collections.nCopies(id.length, "?"))  + ")", id);
    }

    private Subtask getSubtaskByCursor(Cursor cursor) {
        Subtask subtask = new Subtask(context);
        subtask.setId(Integer.parseInt(cursor.getString(0)));
        subtask.setIdTask(Integer.parseInt(cursor.getString(1)));
        subtask.setName(BaseClass.LANGUAGE_DEFAULT, cursor.getString(2));
        subtask.setName(BaseClass.LANGUAGE_EN, cursor.getString(3));
        subtask.setName(BaseClass.LANGUAGE_RU, cursor.getString(4));
        subtask.setAmount(cursor.getDouble(5));
        subtask.setDate(BaseHelper.getDateFromString(cursor.getString(6)));
        subtask.setComplete(Integer.parseInt(cursor.getString(7)) > 0);
        subtask.setUpdate(BaseHelper.getDateFromString(cursor.getString(9)));
        return subtask;
    }
}
