package com.android.viacheslavtimecounter;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.UUID;

public class LastStartedDoingPreferences {
    private static final String PREF_START_TIME = "startTime";
    private static final String PREF_START_DATE = "startDate";
    private static final String PREF_DOING_ID = "last_doing_id";

    public static void setStartTime(Context context, long time, UUID id, String dateStr) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(PREF_START_TIME, time)
                .putString(PREF_DOING_ID, id.toString())
                .putString(PREF_START_DATE, dateStr)
                .apply();
    }

    public static long getStartTimeMillis(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(PREF_START_TIME, 0);
    }
    public static String getStartDate(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_START_DATE, null);
    }

    public static UUID getStartedDoingID(Context context) {
        String stringID = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_DOING_ID, null);
        return UUID.fromString(stringID);
    }

}
