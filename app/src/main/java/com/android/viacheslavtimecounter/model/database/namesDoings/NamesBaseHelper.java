package com.android.viacheslavtimecounter.model.database.namesDoings;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.android.viacheslavtimecounter.model.database.namesDoings.NamesDbSchema.*;

public class NamesBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "NamesDoingsBase.db";


    public NamesBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NamesTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                NamesTable.Cols.UUID + ", " +
                NamesTable.Cols.TITLE + ", " +
                NamesTable.Cols.COLOR +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
