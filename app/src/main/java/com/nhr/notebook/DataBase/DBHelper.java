package com.nhr.notebook.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private String sql;

    public DBHelper(@Nullable Context context) {
        super(context, DBContants.DB_NAME, null, DBContants.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sql = "create table  "
                + DBContants.TABLE_DIARY_NAME + " ( "
                + DBContants.TABLE_FIELD_ID + " integer primary key autoincrement,"
                + DBContants.TABLE_FIELD_TITLE + " varchar(255) , "
                + DBContants.TABLE_FIELD_CONTEXT + " varchar(255) ,"
                + DBContants.TABLE_FIELD_AUTHOR + " varchar(255), "
                +DBContants.TABLE_FIELD_DATE+ " varchar(255))";

        db.execSQL(sql);
        sql = "create table "
                + DBContants.TABLE_PHOTO_NAME + "(  "
                + DBContants.TABLE_FIELD_PHOTO_ID + " integer primary key ,"
                + DBContants.TABLE_FIELD_PHOTO_PATH + "  varchar(255),"
                +DBContants.TABLE_FIELD_PHOTO2DIARY_ID + " integer)";
        Log.e("TAG", "onCreate: "+sql);
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}