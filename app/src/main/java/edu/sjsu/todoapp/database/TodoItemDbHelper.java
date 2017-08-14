package edu.sjsu.todoapp.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by akshaymathur on 8/12/17.
 */

public class TodoItemDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TodoItemEntryContract.TodoItemEntry.TABLE_NAME + " (" +
                    TodoItemEntryContract.TodoItemEntry._ID + " INTEGER PRIMARY KEY," +
                    TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_TITLE + " TEXT," +
                    TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_DESC + " TEXT," +
                    TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_ASSIGNED_TO + " TEXT," +
                    TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_DUE_DATE + " TEXT," +
                    TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_CREATED_DATE + " TEXT," +
                    TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_EDITED_DATE + " TEXT," +
                    TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_PRIORITY + " TEXT" +
                    ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TodoItemEntryContract.TodoItemEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TodoApp.db";

    public TodoItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);

    }
}
