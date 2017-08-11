package com.app.dmitryteplyakov.sportdiary.database.Training;

/**
 * Created by Dmitry on 24.07.2017.
 */

public class TrainingDbSchema {
    public static final class TrainingTable {
        public static final String NAME = "training_title";

        public static final class Cols {
            public static final String TITLE = "training_name";
            public static final String UUID = "training_uuid";
            public static final String PARENTUUID = "training_parent_uuid";
            public static final String PARENTDAYUUID = "training_parent_day_uuid";
        }
    }
}
