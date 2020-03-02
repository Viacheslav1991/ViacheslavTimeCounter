package com.android.viacheslavtimecounter.model.database;

import android.database.Cursor;
import android.database.CursorWrapper;


import com.android.viacheslavtimecounter.model.Doing;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import static com.android.viacheslavtimecounter.model.database.DoingDbSchema.*;


public class DoingCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public DoingCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Doing getDoing() {
        String uuidString = getString(getColumnIndex(DoingsTable.Cols.UUID));
        String title = getString(getColumnIndex(DoingsTable.Cols.TITLE));
        long date = getLong(getColumnIndex(DoingsTable.Cols.DATE));
        int color = getInt(getColumnIndex(DoingsTable.Cols.COLOR));
        int timeInt = getInt(getColumnIndex(DoingsTable.Cols.TOTALTIMEINT));

        Doing doing = new Doing(UUID.fromString(uuidString));
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(date);
        doing.setDate(calendar);
        doing.setTitle(title);
        doing.setColor(color);
        doing.setTotalTimeInt(timeInt);
        return doing;
    }
}
