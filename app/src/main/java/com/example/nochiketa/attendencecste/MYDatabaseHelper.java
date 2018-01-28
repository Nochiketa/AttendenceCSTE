package com.example.nochiketa.attendencecste;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Nochiketa on 1/14/2018.
 */

public class MYDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student.db";
    private static final String TABLE_NAME = "studentpresent";
    private static final String ID = "_id";
    private static final String DATE = "date";
    private static final String PRESENT_STUDENTS = "present_students";
    private static final int VERSION_NUMBER = 2;
    private static final String CREATE_TABLE = "CREATE TABLE "  +TABLE_NAME + "( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE + " VARCHAR(255), " + PRESENT_STUDENTS + " VARCHAR(255));";
    //private static final String CREATE_TABLE = "CREATE TABLE studentpresent(_id INTEGER PRIMARY KEY AUTOINCREMENT, present_students VARCHAR(255));";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    private Context context;

    public MYDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try
        {
            //Toast.makeText(context, "Oncreate is called", Toast.LENGTH_LONG).show();
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }
        catch (Exception i)
        {
            Toast.makeText(context, "Exception: "+i, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try
        {
            //Toast.makeText(context, "OnUpgrade is called", Toast.LENGTH_LONG).show();
            sqLiteDatabase.execSQL(DROP_TABLE);
            onCreate(sqLiteDatabase);
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Exception: "+e, Toast.LENGTH_LONG).show();
        }
    }

    public long insertData(String currentDate, String present_students)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DATE, currentDate);
        contentValues.put(PRESENT_STUDENTS, present_students);

        long rowid = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        return rowid;
    }

    public Cursor displayAllData()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_ALL, null);
        return  cursor;
    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, ID + " = ? ", new String[]{id});
    }
}
