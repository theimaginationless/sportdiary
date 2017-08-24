package com.app.dmitryteplyakov.sportdiary.database.TimerTemplate;

/**
 * Created by dmitry21 on 24.08.17.
 */

public class TimerTemplateDbSchema {
    public static final class TimerTemplateTable {
        public static final String NAME = "timer_templates";
        public static final class Cols {
            public static final String TITLE = "timer_template_title";
            public static final String UUID = "timer_template_uuid";
        }
    }
}
