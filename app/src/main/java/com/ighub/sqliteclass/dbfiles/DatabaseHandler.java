package com.ighub.sqliteclass.dbfiles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contactsManager";
    public static final String TABLE_NAME = "contacts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_FIRSTNAME + " TEXT, "
                + COLUMN_LASTNAME + " TEXT, "
                + COLUMN_PHONE + " TEXT, "
                + COLUMN_EMAIL + " TEXT " + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);

    }

    public void addContact(ContactModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, model.getId());
            values.put(COLUMN_FIRSTNAME, model.getFname());
            values.put(COLUMN_LASTNAME, model.getLname());;
            values.put(COLUMN_EMAIL, model.getEmail());
            values.put(COLUMN_PHONE, model.getPhone());

            db.insert(TABLE_NAME, null, values);

        } finally {
            db.close();
        }
    }

    public ContactModel getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        ContactModel contact = null;

        try {
            cursor = db.query(
                    TABLE_NAME,
                    new String[] {COLUMN_ID, COLUMN_FIRSTNAME, COLUMN_LASTNAME, COLUMN_EMAIL, COLUMN_PHONE},
                    COLUMN_ID + "=?",
                    new String[] { String.valueOf(id) },
                    null,
                    null,
                    null,
                    null);
        } catch (Exception e){
            return null;
        }

        if(cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        try {
            contact = new ContactModel(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
        } catch (CursorIndexOutOfBoundsException e) {
            return null;
        }

        return contact;
    }
}
