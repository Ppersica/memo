package com.example.guistar.memorandum.Database;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UserDBHelper extends SQLiteOpenHelper {
    public static final String CREATE_USERDATA="create table userData(" +
            "name,"
            +"user,"
            +"word)";

    private Context mContext;
public UserDBHelper(Context context) {
    super(context, "memorandum.db", null, 1);
}
    public  void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USERDATA);
    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        //onCreate(db);
    }
}
