package com.example.gauravpc.mobilecomputing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Gaurav Pc on 10/25/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notesDB";  // Database Name

    private static final String TABLE_msg = "msg";  // notes table
    private static final String KEY_ID = "id";
    private static final String sent_by = "from_user";
    private static final String sent_to = "to_user";
    private static final String original_msg = "original_msg";
    private static final String encrpt_key = "encrpt_key";
    private static final String encrpt_msg = "encrpt_msg";
    private static final String date = "date";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_msg + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + sent_by + " TEXT,"
                + sent_to + " TEXT,"
                + original_msg + " TEXT,"
                + encrpt_key + " TEXT,"
                + encrpt_msg + " TEXT,"
                + date + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop notes table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_msg);
        onCreate(db);
    }
    public void addMsg(MsgModel msgModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(sent_by,msgModel.getFrom());
        contentValues.put(sent_to,msgModel.getTo());
        contentValues.put(original_msg,msgModel.getOmsg());
        contentValues.put(encrpt_key,msgModel.getEk());
        contentValues.put(encrpt_msg,msgModel.getEmsg());
        contentValues.put(date,msgModel.getDate());
        db.insert(TABLE_msg,null,contentValues);
        db.close();
    }
    public ArrayList<MsgModel> viewMsg(){
        ArrayList<MsgModel> modelArrayList=new ArrayList<MsgModel>();
        String sq="SELECT * FROM "+TABLE_msg+" ORDER BY date("+date+") DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sq, null);
        if(cursor.moveToFirst()) {
            do {
                MsgModel msgModel=new MsgModel();
                msgModel.setId(cursor.getString(0));
                msgModel.setFrom(cursor.getString(1));
                msgModel.setTo(cursor.getString(2));
                msgModel.setOmsg(cursor.getString(3));
                msgModel.setEk(cursor.getString(4));
                msgModel.setEmsg(cursor.getString(5));
                msgModel.setDate(cursor.getString(6));
                modelArrayList.add(msgModel);
            }while(cursor.moveToNext());
        }
        db.close();
        return modelArrayList;
    }
    public void truncateTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_msg, null, null);
    }
}
