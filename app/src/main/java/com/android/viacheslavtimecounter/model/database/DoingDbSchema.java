package com.android.viacheslavtimecounter.model.database;

public class DoingDbSchema {
    public static final class DoingsTable {
        public static final String NAME = "doings";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String COLOR = "color";
            public static final String TOTALTIMEINT = "time";
        }
    }
}
