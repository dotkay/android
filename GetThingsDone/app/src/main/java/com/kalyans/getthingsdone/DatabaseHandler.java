package com.kalyans.getthingsdone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by kalyans on 2/20/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TaskManager";
    private static final String TASK_TABLE = "tasks";

    // table columns in the database
    private static final String KEY_TASKNAME = "taskname";
    private static final String KEY_TASKPRI = "taskpriority";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // creating db tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASK_TABLE = "CREATE TABLE " + TASK_TABLE + "(" +
                KEY_TASKNAME + " TEXT PRIMARY KEY, " +
                KEY_TASKPRI + " TEXT" +")";
        db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
        onCreate(db);
    }

    // CRUD operations
    public void addTask(TodoTask t) {
        Log.e("DB: addTask(): ", t.taskText);
        // get reference to the db
        SQLiteDatabase db = this.getWritableDatabase();

        // Form ContentValues to insert into our db
        ContentValues values = new ContentValues();
        values.put(KEY_TASKNAME, t.getTaskName());
        values.put(KEY_TASKPRI, t.getTaskPriority());

        // inserting into db row
        db.insert(TASK_TABLE, null, values);
        db.close();
    }

    public ArrayList<TodoTask> getAllTasks() {
        ArrayList<TodoTask> tasks = new ArrayList<TodoTask>();

        // build the query
        String query = "SELECT * FROM " + TASK_TABLE;

        // get reference to db
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // iterate over each row of the db, form the task and add it to our array
        // TodoTask task = null;
        if (cursor.moveToFirst()) {
            do {
                TodoTask task = new TodoTask();
                task.setTaskText(cursor.getString(0));
                task.setTaskPriority(task.getPriority(cursor.getString(1)));
                Log.e("INFO-String(0): ", cursor.getString(0));
                Log.e("INFO-String(1): " ,cursor.getString(1));

                // add task to tasks
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("DB: getAllTasks(): ", Integer.toString(tasks.size()));
        return tasks;
    }

    public int updateTask(TodoTask t) {
        // get reference to db
        SQLiteDatabase db = this.getWritableDatabase();

        // create ContentValue for the task to update
        ContentValues values = new ContentValues();
        values.put(KEY_TASKNAME, t.getTaskName());
        values.put(KEY_TASKPRI, t.getTaskPriority());

        // update the db row
        String where = KEY_TASKNAME +" = ?" + " AND " + KEY_TASKPRI +" = ?";
        String[] whereArgs = new String[] {t.getTaskName(), t.getTaskPriority()};
        int i = db.update(TASK_TABLE, values, where, whereArgs);
        Log.e("INFO-Update: ", t.taskText);

        db.close();

        Log.e("DB: updateTask(): ", t.getTaskName());
        return i;
    }

    public void removeTask(TodoTask t) {
        // get reference to db
        SQLiteDatabase db = this.getWritableDatabase();

        // delete row corresponding to t
        String where = KEY_TASKNAME + " = ?" + " AND "+ KEY_TASKPRI + " = ?";
        String[] whereArgs = new String[] {t.getTaskName(), t.getTaskPriority()};
        db.delete(TASK_TABLE, where, whereArgs);

        Log.e("DB: removeTask(): ", t.getTaskName());
        db.close();
    }
}
