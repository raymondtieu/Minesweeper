package com.raymondtieu.minesweeper.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.raymondtieu.minesweeper.models.Statistic;
import com.raymondtieu.minesweeper.utils.GameUtils;

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

    public void addTime(String difficulty, Long date, Long time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        Log.i("DATABASE", "diff: " + difficulty + ", date: " + date + ", time: " + time);

        values.put(KEY_DATE, date);
        values.put(KEY_TIME, time);

        // insert into table
        if (difficulty == GameUtils.BEGINNER)
            db.insert(TABLE_BEGINNER, null, values);
        else if (difficulty == GameUtils.INTERMEDIATE)
            db.insert(TABLE_INTERMEDIATE, null, values);
        else if (difficulty == GameUtils.ADVANCED)
            db.insert(TABLE_ADVANCED, null, values);

        db.close();
    }

    public Statistic getStatistics(String difficulty) {
        Statistic statistic = new Statistic();
        String table = TABLE_INTERMEDIATE;

        if (difficulty == GameUtils.BEGINNER)
            table = TABLE_BEGINNER;
        else if (difficulty == GameUtils.ADVANCED)
            table = TABLE_ADVANCED;

        setBestRecords(table, statistic);
        setGamesPlayed(table, statistic);
        setGamesWon(table, statistic);
        statistic.calculateWinPercentage();
        setGameStreaks(table, statistic);

        return statistic;
    }


    public void setBestRecords(String table, Statistic statistic) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + table
                + " WHERE " + KEY_TIME + " IS NOT NULL "
                + " ORDER BY " + KEY_TIME + " ASC LIMIT 5";


        Cursor cursor = db.rawQuery(query, null);

        Statistic.Record[] records = new Statistic.Record[5];

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                records[i] = new Statistic.Record(cursor.getLong(DATE), cursor.getLong(TIME));
                i++;
            } while (cursor.moveToNext());
        }

        cursor.close();

        statistic.setBestRecords(records);
    }

    public void setGamesPlayed(String table, Statistic statistic) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + table;

        Cursor cursor = db.rawQuery(query, null);

        int gamesPlayed = cursor.getCount();

        cursor.close();

        statistic.setGamesPlayed(gamesPlayed);
    }

    public void setGamesWon(String table, Statistic statistic) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + table
                + " WHERE " + KEY_TIME + " IS NOT NULL";

        Cursor cursor = db.rawQuery(query, null);

        int gamesWon = cursor.getCount();

        cursor.close();

        statistic.setGamesWon(gamesWon);
    }

    public void setGameStreaks(String table, Statistic statistic) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + table
                + " ORDER BY " + KEY_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        int winStreak = 0;
        int loseStreak = 0;
        int currentStreak = 0;
        boolean isWinStreak = false;

        if (cursor.moveToFirst()) {

            int currentWin = 0;
            int currentLose = 0;
            boolean endStreak = false;

            do {
                // lose if time is null
                if (cursor.isNull(TIME)) {
                    currentLose++;
                    currentWin = 0;

                    if (!endStreak) {
                        if (currentStreak == 0)
                            isWinStreak = false;

                        if (!isWinStreak)
                            currentStreak++;
                        else
                            endStreak = true;
                    }
                } else {
                    currentWin++;
                    currentLose = 0;

                    if (!endStreak) {
                        if (currentStreak == 0)
                            isWinStreak = true;

                        if (isWinStreak)
                            currentStreak++;
                        else
                            endStreak = true;
                    }
                }

                if (currentWin > winStreak)
                    winStreak = currentWin;

                if (currentLose > loseStreak)
                    loseStreak = currentLose;

            } while (cursor.moveToNext());
        }

        statistic.setWinStreak(winStreak);
        statistic.setLoseStreak(loseStreak);

        if (currentStreak == 0)
            statistic.setStreak("0");
        else
            if (isWinStreak)
                statistic.setStreak("" + currentStreak + " W");
            else
                statistic.setStreak("" + currentStreak + " L");
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_BEGINNER, null, null);
        db.delete(TABLE_INTERMEDIATE, null, null);
        db.delete(TABLE_ADVANCED, null, null);

        db.close();
    }
}
