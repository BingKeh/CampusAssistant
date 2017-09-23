package com.example.morho.mytest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Morho on 7/29/2017.
 */

public final class Scoredb {

    private Scoredb() {}

    public static final List<HashMap<String, String>> get_list(SQLiteDatabase db) {
        System.out.println("getting list now ...");
        List<HashMap<String, String>> list = new ArrayList<>();
        String[] projection = {
                ScoreEntry.COLUMN_NAME_SC_YEARS,
                ScoreEntry.COLUMN_NAME_SC_TERM,
                ScoreEntry.COLUMN_NAME_SC_ID,
                ScoreEntry.COLUMN_NAME_SC_NAME,
                ScoreEntry.COLUMN_NAME_SC_CREDITS,
                ScoreEntry.COLUMN_NAME_SC_NORMAL_SC,
                ScoreEntry.COLUMN_NAME_SC_END_SC,
                ScoreEntry.COLUMN_NAME_SC_SC
        };

        String selection = ScoreEntry.COLUMN_NAME_SC_ID + " =?";
        String[] selectionArgs = {
                "*"
        };
        Cursor cs = db.query(
                ScoreEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        for (cs.moveToFirst(); !cs.isAfterLast(); cs.moveToNext()) {
            HashMap<String, String> data = new HashMap<>();
            data.put(ScoreEntry.COLUMN_NAME_SC_YEARS, cs.getString(0) + "学年");
            data.put(ScoreEntry.COLUMN_NAME_SC_TERM, "第" + cs.getString(1) + "学期");
            data.put(ScoreEntry.COLUMN_NAME_SC_ID, String.valueOf(cs.getInt(2)));
            data.put(ScoreEntry.COLUMN_NAME_SC_NAME, cs.getString(3));
            data.put(ScoreEntry.COLUMN_NAME_SC_CREDITS, cs.getString(4));
            data.put(ScoreEntry.COLUMN_NAME_SC_NORMAL_SC, cs.getString(5));
            data.put(ScoreEntry.COLUMN_NAME_SC_END_SC, cs.getString(6));
            data.put(ScoreEntry.COLUMN_NAME_SC_SC, cs.getString(7));
            data.put("STATUS", "TRUE");
            String name = cs.getString(3);
            list.add(data);
            System.out.println(name);
        }
        cs.close();
        return list;
    }

    public static class ScoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "score";
        public static final String COLUMN_NAME_SC_ID = "cid";
        public static final String COLUMN_NAME_SC_NAME = "cname";
        public static final String COLUMN_NAME_SC_SC = "score";
        public static final String COLUMN_NAME_SC_YEARS = "years";
        public static final String COLUMN_NAME_SC_TERM = "term";
        public static final String COLUMN_NAME_SC_CREDITS = "credits";
        public static final String COLUMN_NAME_SC_NORMAL_SC = "nscore";
        public static final String COLUMN_NAME_SC_END_SC  ="escore";

    }
}
