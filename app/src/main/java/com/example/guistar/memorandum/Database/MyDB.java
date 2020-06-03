package com.example.guistar.memorandum.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDB extends SQLiteOpenHelper {
    private static  MyDB dbHelper;
    public final static String TABLE_NAME_RECORD = "record";
    public final static String RECORD_ID = "_id";
    public final static String RECORD_TITLE = "title_name";
    public final static String RECORD_BODY = "text_body";
    public final static String RECORD_TIME = "create_time";


    public MyDB(Context context) {
        super(context, "memoran.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME_RECORD+" ("+RECORD_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                RECORD_TITLE+" VARCHAR2," +
                RECORD_BODY+" VARCHAR2," +
                RECORD_TIME+" VARCHAR2 NOT NULL)");
    }

    public static MyDB getInstance(Context context) {

        if (dbHelper == null) { //单例模式
            dbHelper = new MyDB(context);
        }
        return dbHelper;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}