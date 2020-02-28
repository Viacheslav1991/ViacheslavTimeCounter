package com.android.viacheslavtimecounter.model;

import java.util.Calendar;
import java.util.UUID;

public class Doing {
    private UUID mId;
    private String mTitle;
    private int mColor;
    private int mTotalTimeInt;
    private Calendar mDate;



    public Doing(String title, int color) {
        mId = UUID.randomUUID();
        mTitle = title;
        mColor = color;
        mTotalTimeInt = 0;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public int getTotalTimeInt() {
        return mTotalTimeInt;
    }

    public void setTotalTimeInt(int totalTimeInt) {
        mTotalTimeInt = totalTimeInt;
    }

    public Calendar getDate() {
        return mDate;
    }

    public void setDate(Calendar date) {
        mDate = date;
    }




}
