package com.example.healthy.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="datas.db";
    private static final int DB_VERSION=1;
    MyDatabaseHelper(Context c){
        super(c,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE STATISTICS("
                +"_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"DATE TEXT,"
                +"DISTANCE INTEGER,"
                +"CALORIE INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}