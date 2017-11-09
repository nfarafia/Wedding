package com.vergiliy.wedding.checklist.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vergiliy.wedding.BaseClass;
import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.helpers.SQLiteHelper;
import com.vergiliy.wedding.checklist.summary.Summary;
import com.vergiliy.wedding.checklist.subtask.SubtaskDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskDatabase extends SQLiteHelper {

    private static final String TABLE = "checklist_tasks";
    private static final String COLUMN_ID_CATEGORY = "id_category";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_AMOUNT = "amount";

    // Create access to database
    public TaskDatabase(Context context) {
        super(context);
    }

    @Override
    public String getTable() {
        return TABLE;
    }

    @Override
    protected ContentValues getValues(BaseClass item) {
        Task task = (Task) item;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CATEGORY, task.getIdCategory());
        values.put(COLUMN_NAME, task.getName());
        values.put(COLUMN_NOTE, task.getNote());
        values.put(COLUMN_AMOUNT, task.getAmount());
        return addDefaultContentValues(values, item);
    }

    // Get task by id
    Task getOne(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = String.format(Locale.getDefault(),
                "SELECT c.*, SUM(CASE WHEN s.complete > 0 THEN s.amount ELSE 0 END) paid, " +
                        "SUM(CASE WHEN s.complete == 0 THEN s.amount ELSE 0 END) pending " +
                        "FROM %s c " +
                        "LEFT JOIN %s s ON (s.active = 1 AND c._id = s.id_task) " +
                        "WHERE c.active = 1 AND c._id = %d",
                TABLE, SubtaskDatabase.TABLE, id);

        Task task = null;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            task = getTaskByCursor(cursor);
        }

        cursor.close();
        return task;
    }

    // Get all fields
    private List<Task> getAll(Integer id_category){
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sql = new StringBuilder();

        sql.append(String.format(Locale.getDefault(),
                "SELECT c.*, SUM(CASE WHEN s.complete > 0 THEN s.amount ELSE 0 END) paid, " +
                        "SUM(CASE WHEN s.complete == 0 THEN s.amount ELSE 0 END) pending " +
                        "FROM %s c " +
                        "LEFT JOIN %s s ON (s.active = 1 AND c._id = s.id_task) " +
                        "WHERE c.active = 1 ",
                TABLE, SubtaskDatabase.TABLE));

        if (id_category != null && id_category > 0) {
            sql.append(String.format(Locale.getDefault(), "AND c.id_category = %d ", id_category));
        }

        sql.append("GROUP BY c._id ORDER BY _id ASC");

        List<Task> all = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                Task task = getTaskByCursor(cursor);
                all.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return all;
    }

    // Get balance by Category
    public Summary getBalance(Integer id_category){
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sql = new StringBuilder();

        sql.append(String.format(Locale.getDefault(),
                "SELECT SUM(g.amount) amount, SUM(g.paid) paid, SUM(g.pending) pending, " +
                        "SUM(g.tasks) tasks, SUM(g.subtasks) subtasks, " +
                        "SUM(g.subtasks_complete) subtasks_complete " +
                        "FROM (SELECT c.amount, " +
                        "SUM(CASE WHEN s.complete > 0 THEN s.amount ELSE 0 END) paid, " +
                        "SUM(CASE WHEN s.complete == 0 THEN s.amount ELSE 0 END) pending, " +
                        "COUNT(DISTINCT c._id) tasks, COUNT(DISTINCT s._id) subtasks, " +
                        "SUM(CASE WHEN s.complete > 0 THEN 1 ELSE 0 END) subtasks_complete " +
                        "FROM %s c " +
                        "LEFT JOIN %s s ON (s.active = 1 AND c._id = s.id_task) " +
                        "WHERE c.active = 1 ",
                TABLE, SubtaskDatabase.TABLE));

        if (id_category != null && id_category > 0) {
            sql.append(String.format(Locale.getDefault(), "AND c.id_category = %d ", id_category));
        }

        sql.append("GROUP BY c._id) g");

        Summary balance = new Summary(context);
        Cursor cursor = db.rawQuery(sql.toString(), null);

        if (cursor.moveToFirst()) {
            balance.setAmount(cursor.getDouble(0));
            balance.setPaid(cursor.getDouble(1));
            balance.setPending(cursor.getDouble(2));
            balance.setTasksTotal(cursor.getInt(3));
            balance.setSubtasksTotal(cursor.getInt(4));
            balance.setSubtasksPaid(cursor.getInt(5));
        }

        cursor.close();
        return balance;
    }

    // Get all fields by category id
    public List<Task> getAllByIdCategory(int id_category){
        return getAll(id_category);
    }

    // Get all fields
    private int getCount(Integer id_category){
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sql = new StringBuilder();

        sql.append(String.format(Locale.getDefault(),
                "SELECT COUNT(_id) FROM %s WHERE active = 1 ", TABLE));

        if (id_category != null && id_category > 0) {
            sql.append(String.format(Locale.getDefault(), "AND id_category = %d ", id_category));
        }

        Cursor cursor = db.rawQuery(sql.toString(), null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        cursor.close();

        return 0;
    }

    // Get count fields by category id
    public int getCountByIdCategory(int id_category){
        return getCount(id_category);
    }

    private Task getTaskByCursor(Cursor cursor) {
        Task task = new Task(context);
        task.setId(Integer.parseInt(cursor.getString(0)));
        task.setIdCategory(Integer.parseInt(cursor.getString(1)));
        task.setName(BaseClass.LANGUAGE_DEFAULT, cursor.getString(2));
        task.setName(BaseClass.LANGUAGE_EN, cursor.getString(3));
        task.setName(BaseClass.LANGUAGE_RU, cursor.getString(4));
        task.setNote(BaseClass.LANGUAGE_DEFAULT, cursor.getString(5));
        task.setNote(BaseClass.LANGUAGE_EN, cursor.getString(6));
        task.setNote(BaseClass.LANGUAGE_RU, cursor.getString(7));
        task.setAmount(cursor.getDouble(8));
        task.setUpdate(BaseHelper.getDateFromString(cursor.getString(9)));
        task.setPaid(cursor.getDouble(11));
        task.setPending(cursor.getDouble(12));
        return task;
    }
}
