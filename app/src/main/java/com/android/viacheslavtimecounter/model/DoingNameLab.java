package com.android.viacheslavtimecounter.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.android.viacheslavtimecounter.TimeHelper;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class DoingNameLab {
    private static DoingNameLab sDoingNameLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private List<DoingName> mDoingNames;

    private DoingNameLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = null; //change!
        mDoingNames = new ArrayList<>(); //delete

        // add random doing names
        for (int i = 0; i < 7; i++) {
            DoingName doingName = new DoingName();
            doingName.setTitle(("Doing number " + i));
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            doingName.setColor(color);
            mDoingNames.add(doingName);
        }

    }
    public static DoingNameLab getDoingNameLab(Context context) {
        if (sDoingNameLab == null) {
            sDoingNameLab = new DoingNameLab(context);
        }
        return sDoingNameLab;
    }

    public void addDoingName(DoingName doingName) {
        mDoingNames.add(doingName);
    }

    public void updateDoingName(DoingName doingName) {
        for (int i = 0; i < mDoingNames.size(); i++) {
            if (mDoingNames.get(i).getTitle().equals(doingName.getTitle())) {
                mDoingNames.set(i, doingName);
            }
        }
    }

    public DoingName getDoingName(String title) {
        for (DoingName doingName : mDoingNames
        ) {
            if (doingName.getTitle().equals(title)) {
                return doingName;
            }
        }
        return null;
    }

    public void deleteDoingName(String title) {
        for (DoingName doingName : mDoingNames
        ) {
            if (doingName.getTitle().equals(title)) {
                mDoingNames.remove(doingName);
            }
        }
    }

    public List<DoingName> getDoingNames() {
        return mDoingNames;
    }

}
