package com.raymondtieu.minesweeper.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by raymond on 2015-04-18.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "minesweeper.db";

    private static final String TABLE_BEGINNER = "beginner";
    private static final String TABLE_INTERMEDIATE = "intermediate";
    private static final String TABLE_ADVANCED = "advanced";

    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";

    private static final int DATE = 1;
    private static final int TIME = 2;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // each table consists of 2 columns, a date and time to complete game
        String CREATE_BEGINNER_TABLE =
                "CREATE TABLE " + TABLE_BEGINNER
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + KEY_DATE + " integer, "
                + KEY_TIME + " integer)";

        String CREATE_INTERMEDIATE_TABLE =
                "CREATE TABLE " + TABLE_INTERMEDIATE
                        + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                        + KEY_DATE + " integer, "
                        + KEY_TIME + " integer)";

        String CREATE_ADVANCED_TABLE =
                "CREATE TABLE " + TABLE_ADVANCED
                        + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                        + KEY_DATE + " integer, "
                        + KEY_TIME + " integer)";

        db.execSQL(CREATE_BEGINNER_TABLE);
        db.execSQL(CREATE_INTERMEDIATE_TABLE);
        db.execSQL(CREATE_ADVANCED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addTime(Game.Difficulty difficulty, Long date, Long time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        Log.i("DATABASE", "diff: " + difficulty + ", date: " + date + ", time: " + time);

        values.put(KEY_DATE, date);
        values.put(KEY_TIME, time);

        // insert into table
        if (difficulty == Game.Difficulty.BEGINNER)
            db.insert(TABLE_BEGINNER, null, values);
        else if (difficulty == Game.Difficulty.INTERMEDIATE)
            db.insert(TABLE_INTERMEDIATE, null, values);
        else if (difficulty == Game.Difficulty.ADVANCED)
            db.insert(TABLE_ADVANCED, null, values);

        db.close();
    }



}
