package com.android.viacheslavtimecounter.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.viacheslavtimecounter.TimeHelper;
import com.android.viacheslavtimecounter.model.database.doings.DoingCursorWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static com.android.viacheslavtimecounter.model.database.doings.DoingDbSchema.*;

public class DayStatisticListDoings {
    private Calendar mDate;
    private List<Doing> mDoings;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    public DayStatisticListDoings(Context context, SQLiteDatabase database, Calendar date) {
        mDate = date;
//        mDoings = new ArrayList<>();//download here doings from DB?
        mContext = context;
        mDatabase = database;
        mDoings = getDoings();

    }

    public Calendar getDate() {
        return mDate;
    }//change

    public void addDoing(Doing doing) {
//        mDoings.add(doing);
        ContentValues values = getContentValues(doing);
        mDatabase.insert(DoingsTable.NAME, null, values);
    }

    public Doing getDoing(String title) {
        /*for (Doing doing : mDoings
        ) {
            if (doing.getTitle().equals(title)) {
                return doing;
            }
        }*/

        DoingCursorWrapper cursor = queryDoings(
                DoingsTable.Cols.DATE + " = ?",
                new String[]{TimeHelper.getDateString(mDate)});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Doing doing = cursor.getDoing();
                if (doing.getTitle().equals(title)) {
                    return doing;
                } else
                    cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public Doing getDoing(UUID id) {
        /*for (Doing doing : mDoings
        ) {
            if (doing.getId().equals(id)) {
                return doing;
            }
        }
        return null;*/
        DoingCursorWrapper cursor = queryDoings(
                DoingsTable.Cols.UUID + " = ?",
                new String[]{id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getDoing();
        } finally {
            cursor.close();
        }
    }

    public void updateDoing(Doing doing) {
        /*for (int i = 0; i < mDoings.size(); i++) {
            if (mDoings.get(i).getId().equals(doing.getId())) {
                mDoings.set(i, doing);
            }
        }*/
        String id = doing.getId().toString();
        ContentValues values = getContentValues(doing);
        mDatabase.update(DoingsTable.NAME, values,
                DoingsTable.Cols.UUID + " = ?",
                new String[]{id});

    }

    public List<Doing> getDoings() {

//        return mDoings;
        List<Doing> list = new ArrayList<>();
        DoingCursorWrapper cursor = queryDoings(
                DoingsTable.Cols.DATE + " = ?",
                new String[]{TimeHelper.getDateString(mDate)});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursor.getDoing());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    private ContentValues getContentValues(Doing doing) {
        ContentValues values = new ContentValues();
        values.put(DoingsTable.Cols.UUID, doing.getId().toString());
        values.put(DoingsTable.Cols.TITLE, doing.getTitle());
        values.put(DoingsTable.Cols.DATE, TimeHelper.getDateString(mDate));
        values.put(DoingsTable.Cols.COLOR, doing.getColor());
        values.put(DoingsTable.Cols.TOTALTIMEINT, doing.getTotalTimeInt());
        return values;
    }

    private DoingCursorWrapper queryDoings(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DoingsTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new DoingCursorWrapper(cursor);
    }

    public void deleteDoing(UUID id) {
        for (Doing doing : mDoings
        ) {
            if (doing.getId().equals(id)) {
                mDoings.remove(doing);
            }
        }
    }

    private boolean compareDoingsByTitleAndDate(Doing doing1, Doing doing2) {
/*
        if (doing1.getTitle().equals(doing2.getTitle())
                && TimeHelper.compareDate(doing1.getDate(), doing2.getDate())) {
            return true;
        } else return false;
*/
        return false;
    }

}
