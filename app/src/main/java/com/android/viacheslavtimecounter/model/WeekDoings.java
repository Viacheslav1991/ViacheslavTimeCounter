package com.android.viacheslavtimecounter.model;

import com.android.viacheslavtimecounter.TimeHelper;
import com.android.viacheslavtimecounter.model.database.doings.DoingCursorWrapper;
import com.android.viacheslavtimecounter.model.database.doings.DoingDbSchema;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.android.viacheslavtimecounter.model.StatisticDoingsLab.queryDoings;

public class WeekDoings implements StatisticList {
    private Calendar mCalendar;

    public WeekDoings(Calendar calendar) {
        mCalendar = calendar;
    }

    public List<Doing> getDoings() {
        int dayOfWeek = mCalendar.getFirstDayOfWeek();
        Map<String,Doing> doingHashMap = new HashMap<>();

        for (int i = dayOfWeek; i < (dayOfWeek + 7); i++) {
            mCalendar.set(Calendar.DAY_OF_WEEK, i);
            try (DoingCursorWrapper cursor = queryDoings(
                    DoingDbSchema.DoingsTable.Cols.DATE + " = ?",
                    new String[]{TimeHelper.getDateString(mCalendar)})) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    if (doingHashMap.containsKey(cursor.getDoing().getTitle())) {
                        int time = Objects.requireNonNull(doingHashMap
                                .get(cursor.getDoing().getTitle())).getTotalTimeInt();
                        time += cursor.getDoing().getTotalTimeInt();
                        Objects.requireNonNull(doingHashMap
                                .get(cursor.getDoing().getTitle())).setTotalTimeInt(time);
                    } else {
                        doingHashMap.put(cursor.getDoing().getTitle(), cursor.getDoing());
                    }
                    cursor.moveToNext();
                }
            }
        }
        List<Doing> doings = new ArrayList<>(doingHashMap.values());
        Collections.sort(doings, (o1, o2) -> Integer.compare(o2.getTotalTimeInt(), o1.getTotalTimeInt()));
        return doings;
    }

    @Override
    public Calendar getDate() {
        return mCalendar;
    }
}
