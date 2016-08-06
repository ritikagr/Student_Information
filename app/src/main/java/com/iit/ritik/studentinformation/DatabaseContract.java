package com.iit.ritik.studentinformation;

import android.provider.BaseColumns;
import android.widget.EditText;
import android.view.*;
/**
 * Created by ritik on 8/4/2016.
 */
public final class DatabaseContract {
    DatabaseContract()
    {    }

    public static abstract class DataEntry implements BaseColumns{
        public static final String COLUMN_ID = "adm_no";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email_id";

        public static final String SQL_CREATE_TABLE = "(" +
                DataEntry.COLUMN_ID + " varchar(10) primary key not null," +
                DataEntry.COLUMN_NAME + " varchar(30) not null," +
                DataEntry.COLUMN_EMAIL + " varchar(40) not null)";

        public static final String SQL_DELETE_TABLE = "drop table if exists ";
    }
}
