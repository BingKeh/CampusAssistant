package com.example.morho.mytest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Morho on 7/29/2017.
 */

public class ScoredbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "School.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Scoredb.ScoreEntry.TABLE_NAME + "(" +
                    Scoredb.ScoreEntry.COLUMN_NAME_SC_YEARS + " TEXT, " +
                    Scoredb.ScoreEntry.COLUMN_NAME_SC_TERM + " TEXT, " +
                    Scoredb.ScoreEntry.COLUMN_NAME_SC_ID + " INTERGER, " +
                    Scoredb.ScoreEntry.COLUMN_NAME_SC_NAME + " TEXT, " +
                    Scoredb.ScoreEntry.COLUMN_NAME_SC_SC + " TEXT, " +
                    Scoredb.ScoreEntry.COLUMN_NAME_SC_CREDITS + " TEXT, " +
                    Scoredb.ScoreEntry.COLUMN_NAME_SC_NORMAL_SC + " TEXT, " +
                    Scoredb.ScoreEntry.COLUMN_NAME_SC_END_SC + " TEXT, " +
                    "PRIMARY KEY( " + Scoredb.ScoreEntry.COLUMN_NAME_SC_YEARS + ", " +
                    Scoredb.ScoreEntry.COLUMN_NAME_SC_TERM + ", " +
                    Scoredb.ScoreEntry.COLUMN_NAME_SC_ID + " ) )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Scoredb.ScoreEntry.TABLE_NAME;
    private static final String SQL_CREATE_COURSE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Coursedb.CourseEntity.TABLE_NAME + "(" +
                    Coursedb.CourseEntity.COLUMN_NAME_YEAR + " TEXT, " +
                    Coursedb.CourseEntity.COLUMN_NAME_TERM + " TEXT, " +
                    Coursedb.CourseEntity.COLUMN_NAME_COURSE_NAME + " TEXT, " +
                    Coursedb.CourseEntity.COLUMN_NAME_LOCATION + " TEXT, " +
                    Coursedb.CourseEntity.COLUMN_NAME_TEACHER_NAME + " TEXT, " +
                    Coursedb.CourseEntity.COLUMN_NAME_DAY_OF_WEEK + " INT, " +
                    Coursedb.CourseEntity.COLUMN_NAME_WEEK_TYPE + " TEXT, " +
                    Coursedb.CourseEntity.COLUMN_NAME_BEGIN + " INT, " +
                    Coursedb.CourseEntity.COLUMN_NAME_END + " INT, " +
                    Coursedb.CourseEntity.COLUMN_NAME_TURNS_BEGIN + " INT, " +
                    Coursedb.CourseEntity.COLUMN_NAME_TURNS_END + " INT, " +
                    "PRIMARY KEY( " + Coursedb.CourseEntity.COLUMN_NAME_YEAR + ", " +
                    Coursedb.CourseEntity.COLUMN_NAME_TERM + ", " +
                    Coursedb.CourseEntity.COLUMN_NAME_TEACHER_NAME + ", " +
                    Coursedb.CourseEntity.COLUMN_NAME_BEGIN + ", " +
                    Coursedb.CourseEntity.COLUMN_NAME_END + ", " +
                    Coursedb.CourseEntity.COLUMN_NAME_LOCATION + ", " +
                    Coursedb.CourseEntity.COLUMN_NAME_DAY_OF_WEEK + ", " +
                    Coursedb.CourseEntity.COLUMN_NAME_TURNS_BEGIN + ", " +
                    Coursedb.CourseEntity.COLUMN_NAME_TURNS_END + ", " +
                    Coursedb.CourseEntity.COLUMN_NAME_COURSE_NAME + ", " +
                    Coursedb.CourseEntity.COLUMN_NAME_WEEK_TYPE + ") )";

    public ScoredbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COURSE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
