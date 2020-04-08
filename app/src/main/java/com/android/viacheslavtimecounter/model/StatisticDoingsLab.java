package com.android.viacheslavtimecounter.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.viacheslavtimecounter.TimeHelper;
import com.android.viacheslavtimecounter.model.database.doings.DoingBaseHelper;
import com.android.viacheslavtimecounter.model.database.doings.DoingCursorWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.android.viacheslavtimecounter.model.database.doings.DoingDbSchema.*;

public class StatisticDoingsLab {
    private static StatisticDoingsLab sStatisticDoingsLab;
    private Context mContext;
    static private SQLiteDatabase sDatabase;
    private List<String> mDates;//use to know how many dates we have

    private StatisticDoingsLab(Context context) {
        mContext = context.getApplicationContext();
        sDatabase = new DoingBaseHelper(mContext).getWritableDatabase();
        updateStatisticDates();
    }

    public static  StatisticDoingsLab getStatisticDoingsLab(Context context) {
        if (sStatisticDoingsLab == null) {
            sStatisticDoingsLab = new StatisticDoingsLab(context);
        }
        return sStatisticDoingsLab;
    }


    public DayDoings getDayDoings(Calendar date) {
        return new DayDoings(mContext, sDatabase, date);
    }

    public DayDoings getDayDoings(Integer i) {
        List<String> dates = new ArrayList<>(mDates);
        Calendar calendar = TimeHelper.getDateCalendar(dates.get(i));
        return getDayDoings(calendar);
    }


    private ArrayList<String> getDates() {
        Set<String> setDates = new LinkedHashSet<>();
        DoingCursorWrapper cursor = queryDoings(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                setDates.add(cursor.getDate());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return new ArrayList<>(setDates);
    }

    public void updateStatisticDates() {
        mDates = getDates();
    }

    static DoingCursorWrapper queryDoings(String whereClause, String[] whereArgs) {
        Cursor cursor = sDatabase.query(
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

    public List<StatisticList> getDays() {
        ArrayList<StatisticList> days = new ArrayList<>();
        for (int i = 0; i < getDates().size(); i++) {
            days.add(getDayDoings(i));
        }
        return days;
    }

    public List<StatisticList> getWeeks() {
        ArrayList<StatisticList> weeks = new ArrayList<>();
        Set<Integer> numbersOfWeeks = new HashSet<>();
        for (int i = 0; i < getDates().size(); i++) {
            Calendar calendar = TimeHelper.getDateCalendar(getDates().get(i));
            numbersOfWeeks.add(calendar.get(Calendar.WEEK_OF_YEAR));
        }
        ArrayList<Integer> numbersOfWeeksList = new ArrayList<>(numbersOfWeeks);
        Collections.sort(numbersOfWeeksList);
        for (int i = 0; i < numbersOfWeeks.size(); i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.set(Calendar.WEEK_OF_YEAR, numbersOfWeeksList.get(i));
            weeks.add(new WeekDoings(calendar));
        }
        return weeks;
    }

}
