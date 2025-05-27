package com.example.contactexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "contactExampleDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "contactList";

    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String AGE_COL = "age";
    private static final String SCHOOLS_COL = "schools";
    private static final String NATIONALITY_COL = "nationality";
    private static final String GENDER_COL = "gender";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COL + " TEXT, " +
                AGE_COL + " INTEGER, " +
                SCHOOLS_COL + " TEXT, " +
                NATIONALITY_COL + " TEXT, " +
                GENDER_COL + " TEXT)";
        db.execSQL(query);
    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, contact.getName());
        values.put(AGE_COL, contact.getAge());
        values.put(SCHOOLS_COL, contact.getSchoolNamesString());
        values.put(NATIONALITY_COL, contact.getNationality().name());
        values.put(GENDER_COL, contact.getGender().name());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
