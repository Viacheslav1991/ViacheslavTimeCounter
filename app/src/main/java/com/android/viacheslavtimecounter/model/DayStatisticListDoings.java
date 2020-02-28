package com.android.viacheslavtimecounter.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DayStatisticListDoings {
    private Calendar mDate;
    private List<Doing> mDoings;

    public DayStatisticListDoings() {
        mDate = new GregorianCalendar();
        mDoings = new ArrayList<>();
    }

    public List<Doing> getDoings() {
        return mDoings;
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

}
