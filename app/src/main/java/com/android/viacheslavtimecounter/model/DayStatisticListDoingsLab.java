package com.android.viacheslavtimecounter.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.android.viacheslavtimecounter.TimeHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class DayStatisticListDoingsLab {
    private static DayStatisticListDoingsLab sDayStatisticListDoingsLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private List<DayStatisticListDoings> mDayStatisticListDoingsList;

    private DayStatisticListDoingsLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = null; //change!
        mDayStatisticListDoingsList = new ArrayList<>(); //delete


        for (int i = 0; i < 8; i++) { //add some today's doings
            Doing doing = new Doing(("Doing number " + i), Color.BLUE);
            Random rnd = new Random();
            doing.setTotalTimeInt((rnd.nextInt(86400)));
            getDayStatisticListDoings(new GregorianCalendar())
                    .getDoings()
                    .add(doing);
        }
/*
        for (int i = 0; i < 8; i++) { //add some today's doings
            Doing doing = new Doing(("Doing number " + i), Color.BLUE);
            Random rnd = new Random();
            doing.setTotalTimeInt((rnd.nextInt(86400)));
            Calendar calendar = new GregorianCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, 2);
            getDayStatisticListDoings(calendar)
                    .getDoings()
                    .add(doing);
        }
*/
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
            if (TimeHelper.compareDate(dayStatisticListDoings.getDate(), date)) {
                return dayStatisticListDoings;
            }
        }
        DayStatisticListDoings dayStatisticListDoings = new DayStatisticListDoings(mContext);
        addDayStatisticListDoings(dayStatisticListDoings);
        return dayStatisticListDoings;
    }

    public DayStatisticListDoings getDayStatisticListDoings(Integer i) {
        return mDayStatisticListDoingsList.get(i);
    }

    public int getSize() {
        return mDayStatisticListDoingsList.size();
    }

    public void updateDoing(Doing doing) {

    }//is it need?

    public void deleteDayStatisticListDoings(Calendar date) {

    }//is it need?


    public void updateAllLists() {

       /*
        List<Doing> doings = DoingLab.getDoingLab(mContext).getDoings();
        for (Doing doing : doings)
        {
            if (!getDayStatisticListDoings(doing.getDate()).getDoings().contains(doing)) {
                getDayStatisticListDoings(doing.getDate()).addDoing(doing);
            }
        }*/
    }
}
