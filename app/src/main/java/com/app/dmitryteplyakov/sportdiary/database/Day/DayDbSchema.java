package com.app.dmitryteplyakov.sportdiary.database.Day;

/**
 * Created by Dmitry on 23.07.2017.
 */

public class DayDbSchema {
    public static final class DayTable {
        public static final String NAME = "day_title";

        public static final class Cols {
            public static final String DATE = "day_date";
            public static final String UUID = "day_uuid";
            public static final String TRAININGUUID = "day_training_uuid";
        }
    }
}
