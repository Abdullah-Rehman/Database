package com.abdullah_rehman.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Abdullah_Rehman on 11-Oct-17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "OurDB";
    private static final int DB_VERSION = 2;
    private static final String TABLE_NAME = "records";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";

    SQLiteDatabase myDB;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER NOT NULL, " +
                COLUMN_NAME + " TEXT NOT NULL); ";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addRecord(String record){
        openDB();
        ContentValues row = new ContentValues();
        row.put(COLUMN_NAME, record);
        row.put(COLUMN_ID, 0);

        Long result = myDB.insert(TABLE_NAME, null, row);
        Log.d("DBHelper", result.toString());
    }

    public void openDB(){
        myDB = getWritableDatabase();
    }

    public void closeDB(){
        if(myDB.isOpen() && myDB != null)
            myDB.close();
    }

    public JSONArray getAllRecords() {
        if(myDB==null || !myDB.isOpen()){
            openDB();
        }

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor      = myDB.rawQuery(selectQuery, null);
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        //Log.d("TAG_NAME", e.getMessage());
                    } // End of try-catch
                } // End of if (cursor.getColumnName(i) != null)
            } // End of for-loop
            resultSet.put(rowObject);
            cursor.moveToNext();
        } // End of while-loop
        cursor.close();
        //Log.d("TAG_NAME", resultSet.toString());
        return resultSet;
    }
}
