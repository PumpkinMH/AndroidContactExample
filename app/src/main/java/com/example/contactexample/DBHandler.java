package com.example.contactexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

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

    public ArrayList<Contact> getContacts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursorContacts = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<Contact> contacts = new ArrayList<>();
        if(cursorContacts.moveToFirst()) {
            do {
                String name = cursorContacts.getString(cursorContacts.getColumnIndex(NAME_COL));
                String age = cursorContacts.getString(cursorContacts.getColumnIndex(AGE_COL));

                String schools = cursorContacts.getString(cursorContacts.getColumnIndex(SCHOOLS_COL));
                String[] trimmedSchools = schools.split("\\|");
                ArrayList<School> schoolsList = new ArrayList<>();
                for(String school : trimmedSchools) {
                    if(!school.isEmpty()) {
                        schoolsList.add(new School(school));
                    }
                }
                School[] actualSchools = schoolsList.toArray(new School[0]);

                Nationality nationality = Nationality.valueOf(cursorContacts.getString(cursorContacts.getColumnIndex(NATIONALITY_COL)));
                Gender gender = Gender.valueOf(cursorContacts.getString(cursorContacts.getColumnIndex(GENDER_COL)));
                Contact contact = new Contact(name, age, actualSchools, nationality, gender);
                contacts.add(contact);
            } while(cursorContacts.moveToNext());
        }
        cursorContacts.close();
        return contacts;
    }
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = NAME_COL + " = ? AND " +
                AGE_COL + " = ? AND " +
                SCHOOLS_COL + " = ? AND " +
                NATIONALITY_COL + " = ? AND " +
                GENDER_COL + " = ?";

        String[] whereArgs = {
                contact.getName(),
                String.valueOf(contact.getAge()),
                contact.getSchoolNamesString(),
                contact.getNationality().name(),
                contact.getGender().name()
        };
        if(db.delete(TABLE_NAME, whereClause, whereArgs) == 0) {
            throw new RuntimeException("Failed to delete contact");
        }
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
