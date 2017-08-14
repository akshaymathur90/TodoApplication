package edu.sjsu.todoapp.database;

import android.provider.BaseColumns;

/**
 * Created by akshaymathur on 8/12/17.
 */

public final class TodoItemEntryContract {

    private TodoItemEntryContract(){}

    public static class TodoItemEntry implements BaseColumns{
        public static final String TABLE_NAME = "todoitems";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESC = "description";
        public static final String COLUMN_NAME_DUE_DATE = "duedate";
        public static final String COLUMN_NAME_CREATED_DATE = "createddate";
        public static final String COLUMN_NAME_EDITED_DATE = "editeddate";
        public static final String COLUMN_NAME_ASSIGNED_TO = "assignedto";
        public static final String COLUMN_NAME_PRIORITY = "priority";
    }
}
