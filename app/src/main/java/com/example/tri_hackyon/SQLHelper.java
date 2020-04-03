package com.example.tri_hackyon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class SQLHelper extends SQLiteOpenHelper {
    //defining constants
    public static  final String DATABASE_NAME = "Passwords.db";
    public static  final String TABLE_NAME = "unencrypted";
    public static  final String TABLE2_NAME = "encrypted";
    public static  final String unCOL_1 = "ID";
    public static  final String unCOL_2 = "USERNAME";
    public static  final String unCOL_3 = "PASSWORD";
    public static  final String unCOL_4 = "DOMAIN";

    //initalizer
    public SQLHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //these two create/initalize the DB
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY,USERNAME TEXT,PASSWORD TEXT,DOMAIN TEXT)");
        db.execSQL("create table " + TABLE2_NAME + " (ID INTEGER PRIMARY KEY,USERNAME TEXT,PASSWORD TEXT,DOMAIN TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
        onCreate(db);
    }

    //inserts data into SQL DB, returns bool based on success
    public boolean insertData(String website,String password, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(unCOL_2,username);
        contentValues.put(unCOL_3, password);
        contentValues.put(unCOL_4, website);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean insertEncryptedData(String website,String password, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(unCOL_2,username);
        contentValues.put(unCOL_3, password);
        contentValues.put(unCOL_4, website);
        long result = db.insert(TABLE2_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    //selects all in DB, sends to the list
    public Cursor getUData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getEData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE2_NAME,null);
        return res;
    }

    public boolean deleteData(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        if (ID == 0)
            return false;
        else {
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE ID = " + ID);
            return true;
        }

    }
    public void DeleteDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME );
    }

}
