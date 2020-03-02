package com.android.viacheslavtimecounter.model;

import java.util.UUID;

public class DoingName {
    private String mTitle;
    private int mColor;
    private UUID mUUID;

    public DoingName() {
        mUUID = UUID.randomUUID();
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
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
}
