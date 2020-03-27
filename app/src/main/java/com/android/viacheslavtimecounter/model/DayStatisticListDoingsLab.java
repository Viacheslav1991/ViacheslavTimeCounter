package com.android.viacheslavtimecounter.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.viacheslavtimecounter.TimeHelper;
import com.android.viacheslavtimecounter.model.database.doings.DoingBaseHelper;
import com.android.viacheslavtimecounter.model.database.doings.DoingCursorWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.android.viacheslavtimecounter.model.database.doings.DoingDbSchema.*;

public class DayStatisticListDoingsLab {
    private static DayStatisticListDoingsLab sDayStatisticListDoingsLab;
    private Context mContext;
    static private SQLiteDatabase sDatabase;
//    private List<DayStatisticListDoings> mDayStatisticListDoingsList;
    private List<String> mDates;//use to know how many dates we have

    private DayStatisticListDoingsLab(Context context) {
        mContext = context.getApplicationContext();
        sDatabase = new DoingBaseHelper(mContext)
                .getWritableDatabase();
//        mDayStatisticListDoingsList = new ArrayList<>(); //delete
        updateStatisticDates();
        /*for (int i = 0; i < 8; i++) { //add some yesterday's doings
            Random rnd = new Random();
            Doing doing = new Doing(("Doing number " + i), Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
            doing.setTotalTimeInt((rnd.nextInt(86400)));
            Calendar calendar = new MyCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, 2);
            getDayStatisticListDoings(calendar)
                    .addDoing(doing);
        }
        for (int i = 0; i < 8; i++) { //add some today's doings
            Random rnd = new Random();
            Doing doing = new Doing(("Doing number " + i), Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
            doing.setTotalTimeInt((rnd.nextInt(86400)));
            getDayStatisticListDoings(new MyCalendar())
                    .addDoing(doing);
        }*/
        //random doings
        /*for (int i = 0; i < 10; i++) {
            Doing doing = new Doing();
            doing.setTitle(("Doing number " + i));
            doing.setDate(GregorianCalendar.getInstance());
            Random rnd = new Random();
            doing.setTotalTimeInt((rnd.nextInt(86400)));
            mDoings.add(doing);
        }
        mDoings.get(4).getDate().set(Calendar.DAY_OF_MONTH, 25);
        mDoings.get(5).getDate().set(Calendar.DAY_OF_MONTH, 26);
        mDoings.get(6).getDate().set(Calendar.DAY_OF_MONTH, 27);*/
    }

    public static DayStatisticListDoingsLab getDayStatisticListDoingsLab(Context context) {
        if (sDayStatisticListDoingsLab == null) {
            sDayStatisticListDoingsLab = new DayStatisticListDoingsLab(context);
        }
        return sDayStatisticListDoingsLab;
    }



   /* public void addDayStatisticListDoings(DayStatisticListDoings dayStatisticListDoings) {
        mDayStatisticListDoingsList.add(dayStatisticListDoings);
    }*/

    public DayStatisticListDoings getDayStatisticListDoings(Calendar date) {
        /*for (DayStatisticListDoings dayStatisticListDoings : mDayStatisticListDoingsList
        ) {
            if (TimeHelper.compareDate(dayStatisticListDoings.getDate(), date)) {
                return dayStatisticListDoings;
            }
        }
        DayStatisticListDoings dayStatisticListDoings = new DayStatisticListDoings(mContext, mDatabase, date);//it's only for test
        addDayStatisticListDoings(dayStatisticListDoings);
        return dayStatisticListDoings;*/
        return new DayStatisticListDoings(mContext, sDatabase, date);
    }

    public DayStatisticListDoings getDayStatisticListDoings(Integer i) {
        List<String> dates = new ArrayList<>(mDates);
        Calendar calendar = TimeHelper.getDateCalendar(dates.get(i));
        return getDayStatisticListDoings(calendar);
    }

    public int getSize() {
        return mDates.size();
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

}
