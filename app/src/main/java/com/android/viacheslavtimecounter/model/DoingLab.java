package com.android.viacheslavtimecounter.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.viacheslavtimecounter.TimeHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DoingLab {
    private static DoingLab sDoingLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private List<Doing> mDoings;

    private DoingLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = null; //change!
        mDoings = new ArrayList<>(); //delete


        //random doings
        for (int i = 0; i < 7; i++) {
            Doing doing = new Doing(("Doing number " + i), 0);
            doing.setDate(new GregorianCalendar());
            Random rnd = new Random();
            doing.setTotalTimeInt((rnd.nextInt(86400)));
           mDoings.add(doing);
        }
       mDoings.get(4).getDate().set(Calendar.DAY_OF_MONTH, 25);
       mDoings.get(5).getDate().set(Calendar.DAY_OF_MONTH, 26);
       mDoings.get(6).getDate().set(Calendar.DAY_OF_MONTH, 27);
    }

    public static DoingLab getDoingLab(Context context) {
        if (sDoingLab == null) {
            sDoingLab = new DoingLab(context);
        }
        return sDoingLab;
    }

    public void addDoing(Doing doing) {
        mDoings.add(doing);
    }

    public void updateDoing(Doing doing) {
        for (int i = 0; i < mDoings.size(); i++) {
            if (compareDoingsByTitleAndDate(mDoings.get(i), doing)) {
                mDoings.set(i, doing);
            }
        }
    }

    public Doing getDoing(UUID id) {
        for (Doing doing : mDoings
        ) {
            if (doing.getId().equals(id)) {
                return doing;
            }
        }
        return null;
    }

    public void deleteDoing(UUID id) {
        for (Doing doing : mDoings
        ) {
            if (doing.getId().equals(id)) {
                mDoings.remove(doing);
            }
        }
    }

    public List<Doing> getDoings() {
        return mDoings;
    }

    private boolean compareDoingsByTitleAndDate(Doing doing1, Doing doing2) {
        if (doing1.getTitle().equals(doing2.getTitle())
                && TimeHelper.compareDate(doing1.getDate(), doing2.getDate())) {
            return true;
        } else return false;
    }
}
