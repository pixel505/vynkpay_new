package com.vynkpay.SQLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.vynkpay.BuildConfig;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "FILES.db";
    public static final String FILE_TABLE_NAME = "table_name";
    public static final String FILE_COLUMN_ID = "c_id";
    public static final String FILE_ID = "file_id";
    public static final String FILE_NAME = "file_name";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, BuildConfig.VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table " + FILE_TABLE_NAME +" "+ "("+FILE_COLUMN_ID+"  integer primary key, "+FILE_ID+" text, "+FILE_NAME+" text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+FILE_TABLE_NAME);
        onCreate(db);
    }

    public void clearTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ FILE_TABLE_NAME);
    }


    public void insertFile(String id, String fileName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FILE_ID, id);
        contentValues.put(FILE_NAME, fileName);
        db.insert(FILE_TABLE_NAME, null, contentValues);
    }

    public boolean hasFileWithSameName(String fileName) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + FILE_TABLE_NAME + " WHERE " + FILE_NAME + " =?";

        Cursor cursor = db.rawQuery(selectString, new String[] {fileName});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }


    public Cursor getOneItem(String fileName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from "+FILE_TABLE_NAME+" where "+FILE_NAME+"="+fileName+"", null );
    }

}
