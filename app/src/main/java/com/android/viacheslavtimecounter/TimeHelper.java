package com.android.viacheslavtimecounter;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import static java.util.Calendar.DAY_OF_WEEK;

public class TimeHelper {
    private static final SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy");

    public static String getTime(int time) {
        String result = "";
        int hours = time / 3600;
        int minutes = (time - (hours * 3600)) / 60;
        int seconds = (time - (hours * 3600) - (minutes * 60));
        result = String.valueOf(hours) + ":";
        if (minutes < 10) {
            result += "0" + String.valueOf(minutes) + ":";
        } else {
            result += String.valueOf(minutes) + ":";
        }
        if (seconds < 10) {
            result += "0" + String.valueOf(seconds);
        } else {
            result += String.valueOf(seconds);
        }
        return result;
    }

    public static boolean compareDate(Calendar calendar1, Calendar calendar) {
        return calendar1.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH) &&
                calendar1.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
    }

    public static String getDateString(Calendar calendar) {
        return format.format(calendar.getTime());
    }

    public static Calendar getDateCalendar(String strDate) {

        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String getWeekDateString(Calendar date) {
        date.set(DAY_OF_WEEK, date.getFirstDayOfWeek());
        int beginningOfWeek = date.get(Calendar.DAY_OF_MONTH);
        date.set(DAY_OF_WEEK, date.getFirstDayOfWeek() + 6);
        int weekend = date.get(Calendar.DAY_OF_MONTH);
        return String.format(Locale.ENGLISH,"%s from %d to %d"
                , date.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("en"))
                , beginningOfWeek, weekend);
    }
}
