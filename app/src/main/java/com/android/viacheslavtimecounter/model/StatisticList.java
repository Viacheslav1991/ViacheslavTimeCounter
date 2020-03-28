package com.android.viacheslavtimecounter.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public interface StatisticList extends Serializable {
     List<Doing> getDoings();
     Calendar getDate();
}
