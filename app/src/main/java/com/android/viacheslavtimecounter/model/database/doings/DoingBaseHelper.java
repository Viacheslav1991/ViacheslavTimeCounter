package com.android.viacheslavtimecounter.model.database.doings;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.android.viacheslavtimecounter.model.database.doings.DoingDbSchema.*;

public class DoingBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "doingBase.db";


    public DoingBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DoingsTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                DoingsTable.Cols.UUID + ", " +
                DoingsTable.Cols.TITLE + ", " +
                DoingsTable.Cols.DATE + ", " +
                DoingsTable.Cols.COLOR + ", " +
                DoingsTable.Cols.TOTALTIMEINT +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
