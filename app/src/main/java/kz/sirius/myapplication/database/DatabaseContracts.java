package kz.sirius.myapplication.database;

import static android.provider.BaseColumns._ID;

public class DatabaseContracts {

    public static final String TABLE_NAME = "users_table";
    public static final String APPS_TABLE_NAME = "apps";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_NAME + " TEXT," +
                    COLUMN_NAME_PHONE_NUMBER + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String SQL_CREATE_APPS_TABLE = "CREATE TABLE " + APPS_TABLE_NAME + "(" +
            "ID INTEGER PRIMARY KEY, " +
            "TITLE TEXT DEFAULT ''," +
            "DESCRIPTION TEXT DEFAULT '',"+
            "PRICE INTEGER DEFAULT 0,";

    public static final String SQL_DELETE_APPS =
            "DROP TABLE IF EXISTS " + APPS_TABLE_NAME;
}
