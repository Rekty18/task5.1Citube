package com.example.itube;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "iTubeDatabase.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PLAYLISTS = "playlists";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_VIDEO_URL = "video_url";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_FULL_NAME + " TEXT," +
                        COLUMN_EMAIL + " TEXT UNIQUE," +
                        COLUMN_PASSWORD + " TEXT)"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_PLAYLISTS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_EMAIL + " TEXT," +
                        COLUMN_VIDEO_URL + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(
                    "CREATE TABLE " + TABLE_PLAYLISTS + " (" +
                            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                            COLUMN_EMAIL + " TEXT," +
                            COLUMN_VIDEO_URL + " TEXT)"
            );
        }
    }

    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[] {COLUMN_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public boolean addUser(String fullName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public void addUrl(String userEmail, String videoUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, userEmail);
        values.put(COLUMN_VIDEO_URL, videoUrl);
        db.insert(TABLE_PLAYLISTS, null, values);
        db.close();
    }

    public ArrayList<String> getPlaylist(String userEmail) {
        ArrayList<String> playlist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_PLAYLISTS,
                new String[]{COLUMN_VIDEO_URL},
                COLUMN_EMAIL + "=?",
                new String[]{userEmail},
                null, null, null
        );
        while (cursor.moveToNext()) {
            playlist.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return playlist;
    }

    public boolean checkUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_EMAIL},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null
        );
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }
}
