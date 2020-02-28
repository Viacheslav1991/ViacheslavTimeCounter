package com.android.viacheslavtimecounter.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.viacheslavtimecounter.TimeHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DayStatisticListDoingsLab {
    private static DayStatisticListDoingsLab sDayStatisticListDoingsLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private List<DayStatisticListDoings> mDayStatisticListDoingsList;

    private DayStatisticListDoingsLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = null; //change!
        mDayStatisticListDoingsList = new ArrayList<>(); //delete


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

    public static DayStatisticListDoingsLab getDayStatisticListDoingsLab (Context context) {
        if (sDayStatisticListDoingsLab == null) {
            sDayStatisticListDoingsLab = new DayStatisticListDoingsLab(context);
            sDayStatisticListDoingsLab.updateAllLists();
        }
        return sDayStatisticListDoingsLab;
    }

    public void addDayStatisticListDoings(DayStatisticListDoings dayStatisticListDoings) {
        mDayStatisticListDoingsList.add(dayStatisticListDoings);
    }

    public DayStatisticListDoings getDayStatisticListDoings(Calendar date) {
        for (DayStatisticListDoings dayStatisticListDoings : mDayStatisticListDoingsList
        ) {
            if (TimeHelper.compareDate(dayStatisticListDoings.getDate(),date)) {
                return dayStatisticListDoings;
            }
        }
        DayStatisticListDoings dayStatisticListDoings = new DayStatisticListDoings();
        addDayStatisticListDoings(dayStatisticListDoings);
        return dayStatisticListDoings;
    }

    public void updateDoing(Doing doing) {

    }//is it need?

    public void deleteDayStatisticListDoings(Calendar date) {

    }//is it need?

    public void updateAllLists() {
        List<Doing> doings = DoingLab.getDoingLab(mContext).getDoings();
        for (Doing doing : doings)
        {
            if (!getDayStatisticListDoings(doing.getDate()).getDoings().contains(doing)) {
                getDayStatisticListDoings(doing.getDate()).addDoing(doing);
            }
        }
    }
}
