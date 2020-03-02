package com.android.viacheslavtimecounter;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MyCalendar extends GregorianCalendar {

    public MyCalendar() {
        super();
        this.set(GregorianCalendar.HOUR_OF_DAY, 0);
        this.set(GregorianCalendar.HOUR, 0);
        this.set(GregorianCalendar.MINUTE, 0);
        this.set(GregorianCalendar.SECOND, 0);
        this.set(GregorianCalendar.MILLISECOND, 0);
    }
}
