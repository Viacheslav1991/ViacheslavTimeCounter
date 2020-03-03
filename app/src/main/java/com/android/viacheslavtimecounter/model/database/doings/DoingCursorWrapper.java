package com.android.viacheslavtimecounter.model.database.doings;

import android.database.Cursor;
import android.database.CursorWrapper;


import com.android.viacheslavtimecounter.TimeHelper;
import com.android.viacheslavtimecounter.model.Doing;
import com.android.viacheslavtimecounter.MyCalendar;

import java.util.Calendar;
import java.util.UUID;

import static com.android.viacheslavtimecounter.model.database.doings.DoingDbSchema.*;


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
        String date = getString(getColumnIndex(DoingsTable.Cols.DATE));//attention!!!

        int color = getInt(getColumnIndex(DoingsTable.Cols.COLOR));
        int timeInt = getInt(getColumnIndex(DoingsTable.Cols.TOTALTIMEINT));

        Doing doing = new Doing(UUID.fromString(uuidString));
        Calendar calendar = new MyCalendar();
        calendar.setTimeInMillis(TimeHelper.getDateCalendar(date).getTimeInMillis());

//        doing.setDate(calendar);
        doing.setTitle(title);
        doing.setColor(color);
        doing.setTotalTimeInt(timeInt);
        return doing;
    }

    public String getDate() {
        String date = getString(getColumnIndex(DoingsTable.Cols.DATE));
        return date;
    }
}
