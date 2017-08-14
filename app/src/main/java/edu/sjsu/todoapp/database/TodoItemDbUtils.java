package edu.sjsu.todoapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.todoapp.model.ToDoItem;

/**
 * Created by akshaymathur on 8/12/17.
 */

public class TodoItemDbUtils {

    public static List<ToDoItem> fetchAllItems(SQLiteDatabase db){
        String[] projection = {
                TodoItemEntryContract.TodoItemEntry._ID,
                TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_TITLE,
                TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_DESC,
                TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_EDITED_DATE,
                TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_CREATED_DATE,
                TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_DUE_DATE,
                TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_ASSIGNED_TO,
                TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_PRIORITY
        };
        String sortOrder =
                TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_PRIORITY + " DESC";

        Cursor cursor = db.query(
                TodoItemEntryContract.TodoItemEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<ToDoItem> toDoItems = new ArrayList<>();
        while(cursor.moveToNext()) {
            ToDoItem item = new ToDoItem();
            item.setID(cursor.getLong(
                    cursor.getColumnIndexOrThrow(TodoItemEntryContract.TodoItemEntry._ID)));
            item.setItemName(cursor.getString(cursor.getColumnIndexOrThrow(
                    TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_TITLE)));
            item.setAssignedTo(cursor.getString(cursor.getColumnIndexOrThrow(
                    TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_ASSIGNED_TO)));
            item.setDateCreated(cursor.getString(cursor.getColumnIndexOrThrow(
                    TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_CREATED_DATE)));
            item.setDateDue(cursor.getString(cursor.getColumnIndexOrThrow(
                    TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_DUE_DATE)));
            item.setDateEdited(cursor.getString(cursor.getColumnIndexOrThrow(
                    TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_EDITED_DATE)));
            item.setItemDescription(cursor.getString(cursor.getColumnIndexOrThrow(
                    TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_DESC)));
            item.setPriority(cursor.getInt(cursor.getColumnIndexOrThrow(
                    TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_PRIORITY)));
            toDoItems.add(item);
        }
        cursor.close();

        return toDoItems;

    }

    public static long addNewItem(ToDoItem toDoItem, SQLiteDatabase db){

        ContentValues values = new ContentValues();
        values.put(TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_TITLE, toDoItem.getItemName());
        values.put(TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_DESC, toDoItem.getItemDescription());
        values.put(TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_ASSIGNED_TO, toDoItem.getAssignedTo());
        values.put(TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_CREATED_DATE, toDoItem.getDateCreated());
        values.put(TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_DUE_DATE, toDoItem.getDateDue());
        values.put(TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_EDITED_DATE, toDoItem.getDateEdited());
        values.put(TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_PRIORITY, toDoItem.getPriority());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TodoItemEntryContract.TodoItemEntry.TABLE_NAME, null, values);
        return newRowId;
    }

    public static void deleteItem(SQLiteDatabase db, long id){

        // Define 'where' part of query.
        String selection = TodoItemEntryContract.TodoItemEntry._ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(id)};
        // Issue SQL statement.
        db.delete(TodoItemEntryContract.TodoItemEntry.TABLE_NAME, selection, selectionArgs);

    }

    public static int updateItem(SQLiteDatabase db, ToDoItem toDoItem){
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_TITLE, toDoItem.getItemName());
        values.put(TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_DESC, toDoItem.getItemDescription());
        values.put(TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_ASSIGNED_TO, toDoItem.getAssignedTo());
        values.put(TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_CREATED_DATE, toDoItem.getDateCreated());
        values.put(TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_DUE_DATE, toDoItem.getDateDue());
        values.put(TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_EDITED_DATE, toDoItem.getDateEdited());
        values.put(TodoItemEntryContract.TodoItemEntry.COLUMN_NAME_PRIORITY, toDoItem.getPriority());

        // Which row to update, based on the title
        String selection = TodoItemEntryContract.TodoItemEntry._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(toDoItem.getID())};

        int count = db.update(
                TodoItemEntryContract.TodoItemEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;

    }
}
