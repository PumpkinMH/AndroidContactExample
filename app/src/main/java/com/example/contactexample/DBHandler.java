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
    private static final String NATIONALITY_COL = "nationality";
    private static final String GENDER_COL = "gender";

    private static final String SCHOOL_TABLE_NAME = "schoolList";
    private static final String SCHOOL_ID_COL = "id";
    private static final String SCHOOL_NAME_COL = "name";
    private static final String SCHOOL_LOCATION_COL = "location";
    private static final String CS_TABLE_NAME = "contactSchools";
    private static final String CS_CONTACT_ID_COL = "contact_id";
    private static final String CS_SCHOOL_ID_COL = "school_id";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COL + " TEXT, " +
                AGE_COL + " INTEGER, " +
                NATIONALITY_COL + " TEXT, " +
                GENDER_COL + " TEXT)";
        db.execSQL(query);

        String query2 = "CREATE TABLE " + SCHOOL_TABLE_NAME + " (" +
                SCHOOL_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SCHOOL_NAME_COL + " TEXT, " +
                SCHOOL_LOCATION_COL + " TEXT)";
        db.execSQL(query2);

        String query3 = "CREATE TABLE " + CS_TABLE_NAME + " (" +
                CS_CONTACT_ID_COL + " INTEGER, " +
                CS_SCHOOL_ID_COL + " INTEGER, " +
                "PRIMARY KEY (" + CS_CONTACT_ID_COL + ", " + CS_SCHOOL_ID_COL + "), " +
                "FOREIGN KEY (" + CS_CONTACT_ID_COL + ") REFERENCES " + TABLE_NAME + "(" + ID_COL + "), " +
                "FOREIGN KEY (" + CS_SCHOOL_ID_COL + ") REFERENCES " + SCHOOL_TABLE_NAME + "(" + SCHOOL_ID_COL + ") ON DELETE CASCADE)";
        db.execSQL(query3);

    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // Start a transaction for atomicity

        try {
            ContentValues contactValues = new ContentValues();
            contactValues.put(NAME_COL, contact.getName());
            contactValues.put(AGE_COL, contact.getAge());
            contactValues.put(NATIONALITY_COL, contact.getNationality().name());
            contactValues.put(GENDER_COL, contact.getGender().name());

            long contactId = db.insert(TABLE_NAME, null, contactValues);
            if (contactId == -1) {
                // Handle error: contact insertion failed
                // You might throw an exception here
                throw new RuntimeException("Failed to insert contact");
            }
            contact.setId(contactId); // Set the generated ID back to the contact object

            // Link contact to schools in the junction table
            long[] schoolIds = contact.getSchoolIds(); // Get the IDs once
            if (schoolIds != null && schoolIds.length > 0) { // Check for null and empty
                for (long schoolId : schoolIds) {
                    ContentValues junctionValues = new ContentValues();
                    junctionValues.put(CS_CONTACT_ID_COL, contactId);
                    junctionValues.put(CS_SCHOOL_ID_COL, schoolId);
                    long junctionId = db.insert(CS_TABLE_NAME, null, junctionValues);
                    if (junctionId == -1) {
                        throw new RuntimeException("Failed to insert junction entry");
                    }
                }
            }
            db.setTransactionSuccessful(); // Mark the transaction as successful
        } finally {
            db.endTransaction(); // End the transaction (commit if successful, rollback otherwise)
            db.close(); // Close the database
        }
    }

    public ArrayList<Contact> getContacts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursorContacts = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<Contact> contacts = new ArrayList<>();
        if(cursorContacts.moveToFirst()) {
            do {
                String name = cursorContacts.getString(cursorContacts.getColumnIndex(NAME_COL));
                String age = cursorContacts.getString(cursorContacts.getColumnIndex(AGE_COL));
                Nationality nationality = Nationality.valueOf(cursorContacts.getString(cursorContacts.getColumnIndex(NATIONALITY_COL)));
                Gender gender = Gender.valueOf(cursorContacts.getString(cursorContacts.getColumnIndex(GENDER_COL)));
                long id = cursorContacts.getLong(cursorContacts.getColumnIndex(ID_COL));

                ArrayList<Long> schoolIds = new ArrayList<>();
                String junctionQuery = "SELECT " + CS_SCHOOL_ID_COL + " FROM " + CS_TABLE_NAME + " WHERE " + CS_CONTACT_ID_COL + " = ?";
                Cursor cursorJunction = db.rawQuery(junctionQuery, new String[]{String.valueOf(id)});
                if(cursorJunction.moveToFirst()) {
                    do {
                        long schoolId = cursorJunction.getLong(cursorJunction.getColumnIndex(CS_SCHOOL_ID_COL));
                        schoolIds.add(schoolId);
                    } while (cursorJunction.moveToNext());

                }
                cursorJunction.close();

                long[] actualSchoolIds = new long[schoolIds.size()];
                for (int i = 0; i < schoolIds.size(); i++) {
                    actualSchoolIds[i] = schoolIds.get(i);
                }


                Contact contact = new Contact(name, age, nationality, gender, actualSchoolIds, id);
                contacts.add(contact);
            } while(cursorContacts.moveToNext());
        }
        cursorContacts.close();
        return contacts;
    }
    public ArrayList<School> getSchools() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursorSchools = db.rawQuery("SELECT * FROM " + SCHOOL_TABLE_NAME, null);
        ArrayList<School> schools = new ArrayList<>();

        if(cursorSchools.moveToFirst()) {
            do {
                String name = cursorSchools.getString(cursorSchools.getColumnIndex(SCHOOL_NAME_COL));
                Nationality location = Nationality.valueOf(cursorSchools.getString(cursorSchools.getColumnIndex(SCHOOL_LOCATION_COL)));
                long id = cursorSchools.getLong(cursorSchools.getColumnIndex(SCHOOL_ID_COL));
                School school = new School(name, location, id);
                schools.add(school);
            } while(cursorSchools.moveToNext());
        }
        cursorSchools.close();
        return schools;
    }

    public void addSchool(School school) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCHOOL_NAME_COL, school.getName());
        values.put(SCHOOL_LOCATION_COL, school.getLocation().name());
        long id = db.insert(SCHOOL_TABLE_NAME, null, values);
        school.setId(id);
        db.close();
    }
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ID_COL + " = ?";

        String[] whereArgs = {
                String.valueOf(contact.getId())
        };
        if(db.delete(TABLE_NAME, whereClause, whereArgs) == 0) {
            throw new RuntimeException("Failed to delete contact");
        }
        db.close();
    }

    public void editContact(Contact originalContact, Contact newContactData) {
        if(originalContact == null || newContactData == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }
        if(originalContact.getId() == -1) {
            throw new IllegalArgumentException("Contact id cannot be -1");
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(NAME_COL, newContactData.getName());
            values.put(AGE_COL, newContactData.getAge());

            values.put(NATIONALITY_COL, newContactData.getNationality().name());
            values.put(GENDER_COL, newContactData.getGender().name());

            if(db.update(TABLE_NAME, values, ID_COL + " = ?", new String[]{String.valueOf(originalContact.getId())}) == 0) {
                throw new RuntimeException("Failed to edit contact");
            }

            // 2. Update school associations in the contact_schools junction table

            // 2a. Delete all existing school associations for this contact
            db.delete(CS_TABLE_NAME, CS_CONTACT_ID_COL + " = ?", new String[]{String.valueOf(originalContact.getId())});

            // 2b. Insert all new school associations from newContactData
            long[] newSchoolIds = newContactData.getSchoolIds();
            if (newSchoolIds != null && newSchoolIds.length > 0) {
                for (long schoolId : newSchoolIds) {
                    ContentValues junctionValues = new ContentValues();
                    junctionValues.put(CS_CONTACT_ID_COL, originalContact.getId()); // Use originalContact's ID
                    junctionValues.put(CS_SCHOOL_ID_COL, schoolId);
                    long junctionId = db.insert(CS_TABLE_NAME, null, junctionValues);
                    if (junctionId == -1) {
                        throw new RuntimeException("Failed to insert new school association for contact ID " + originalContact.getId() + " and school ID " + schoolId);
                    }
                }
            }

            db.setTransactionSuccessful(); // Mark the transaction as successful
        } finally {
            db.endTransaction(); // End the transaction (commit if successful, rollback otherwise)
            db.close();
        }
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
