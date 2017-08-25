package com.app.dmitryteplyakov.sportdiary.database.Timer;

/**
 * Created by dmitry21 on 24.08.17.
 */

public class TimerDbSchema {
    public static final class TimerTable {
        public static final String NAME = "timers";
        public static final class Cols {
            public static final String TITLE = "timer_title";
            public static final String UUID = "timer_uuid";
            public static final String PARENTUUID = "timer_parent_uuid";
            public static final String ITERATIONS = "timer_iterations";
            public static final String PREPARING = "timer_preparing";
            public static final String WORKOUT = "timer_workout";
            public static final String REST = "timer_rest";
            public static final String SETS = "timer_sets";
            public static final String RESTBETWEENSETS = "timer_rest_between_sets";
            public static final String CALMDOWN = "timer_calm_down";
            public static final String ISCONNECTED = "timer_is_connected";
        }
    }
}
