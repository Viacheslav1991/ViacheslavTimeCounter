package com.android.viacheslavtimecounter.model;

import android.content.Context;

import com.android.viacheslavtimecounter.TimeHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

public class DayStatisticListDoings {
    private Calendar mDate;
    private List<Doing> mDoings;
    private Context mContext;

    public DayStatisticListDoings(Context context) {
        mDate = new GregorianCalendar();
        mDoings = new ArrayList<>();
        mContext = context;
    }


    public Calendar getDate() {
        return mDate;
    }

    public void addDoing(Doing doing) {
        mDoings.add(doing);
    }

    public Doing getDoing(String title) {
        for (Doing doing : mDoings
        ) {
            if (doing.getTitle().equals(title)) {
                return doing;
            }
        }
        return null;
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

    public void updateDoing(Doing doing) {
        for (int i = 0; i < mDoings.size(); i++) {
            if (compareDoingsByTitleAndDate(mDoings.get(i), doing)) {
                mDoings.set(i, doing);
            }
        }
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
