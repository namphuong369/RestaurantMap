package com.nam.restaurantmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LocationDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Location.db";
    private static final int DATABASE_VERSION = 1;

    private static LocationDbHelper instance;
    private SQLiteDatabase db;
    public LocationDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static synchronized LocationDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new LocationDbHelper(context.getApplicationContext());
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;
        final String SQL_CREATE_TITLE_TABLE = "CREATE TABLE " +
                LocationContract.LocationTable.TABLE_NAME + " ( " +
                LocationContract.LocationTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LocationContract.LocationTable.COLUMN_NAME + " TEXT, "+
                LocationContract.LocationTable.COLUMN_LOCATION + " TEXT "+ " ) ";
        db.execSQL(SQL_CREATE_TITLE_TABLE);
    }
    public void addLocation(Item content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LocationContract.LocationTable.COLUMN_NAME, content.getName());
        cv.put(LocationContract.LocationTable.COLUMN_LOCATION, content.getLocation());
        db.insert(LocationContract.LocationTable.TABLE_NAME, null, cv);
    }

    public List<Item> getAllLocation() {
        List<Item> list = new ArrayList<>();
        db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + LocationContract.LocationTable.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                Item title = new Item();
                title.setId((c.getInt(0)));
                title.setName(c.getString(1));
                title.setLocation(c.getString(2));
                list.add(title);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + LocationContract.LocationTable.TABLE_NAME);

        onCreate(db);
    }
    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
            super.close();
        }
    }
}