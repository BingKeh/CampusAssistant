package com.example.morho.mytest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Morho on 8/18/2017.
 */

public class Coursedb {

    public Coursedb() {

    }

    public static List<CourseItem> get_cs_list(SQLiteDatabase db) {
        List<CourseItem> list = new ArrayList<CourseItem>();
        String[] projection = {
                CourseEntity.COLUMN_NAME_YEAR,
                CourseEntity.COLUMN_NAME_TERM,
                CourseEntity.COLUMN_NAME_COURSE_NAME,
                CourseEntity.COLUMN_NAME_TEACHER_NAME,
                CourseEntity.COLUMN_NAME_LOCATION,
                CourseEntity.COLUMN_NAME_DAY_OF_WEEK,
                CourseEntity.COLUMN_NAME_TURNS_BEGIN,
                CourseEntity.COLUMN_NAME_TURNS_END,
                CourseEntity.COLUMN_NAME_WEEK_TYPE,
                CourseEntity.COLUMN_NAME_BEGIN,
                CourseEntity.COLUMN_NAME_END
        };

        String selection = CourseEntity.COLUMN_NAME_YEAR + " = ? and " +
                CourseEntity.COLUMN_NAME_TERM + " = ?";

        String[] selectionArgs = { "2017-2018", "1" };

        Cursor cs = db.query(
                CourseEntity.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        for (cs.moveToFirst(); !cs.isAfterLast(); cs.moveToNext()) {
            CourseItem courseItem = new CourseItem();
            courseItem.setYear(cs.getString(0));
            courseItem.setTerm(cs.getString(1));
            courseItem.setName(cs.getString(2));
            courseItem.setTeacher(cs.getString(3));
            courseItem.setLocation(cs.getString(4));
            courseItem.setDay_of_week(cs.getInt(5));
            courseItem.setTurns_begin(cs.getInt(6));
            courseItem.setTurns_end(cs.getInt(7));
            courseItem.setWeek_type(cs.getString(8));
            courseItem.setBegin(cs.getInt(9));
            courseItem.setEnd(cs.getInt(10));
            list.add(courseItem);
        }
        cs.close();
        return list;
    }


    public class CourseEntity implements BaseColumns {
        public static final String TABLE_NAME = "course";
        public static final String COLUMN_NAME_YEAR = "cyear";
        public static final String COLUMN_NAME_TERM = "cterm";
        public static final String COLUMN_NAME_COURSE_NAME = "cname";
        public static final String COLUMN_NAME_TEACHER_NAME = "tname";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_DAY_OF_WEEK = "day";
        public static final String COLUMN_NAME_WEEK_TYPE = "type";
        public static final String COLUMN_NAME_TURNS_BEGIN = "turns_begin";
        public static final String COLUMN_NAME_TURNS_END = "turns_end";
        public static final String COLUMN_NAME_BEGIN = "begin";
        public static final String COLUMN_NAME_END = "end";
    }
}
