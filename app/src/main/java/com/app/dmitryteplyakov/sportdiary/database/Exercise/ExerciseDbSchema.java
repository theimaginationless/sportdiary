package com.app.dmitryteplyakov.sportdiary.database.Exercise;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class ExerciseDbSchema {
    public static final class ExerciseTable {
        public static final String NAME = "exercises";

        public static final class Cols {
            public static final String TITLE = "exercise_title";
            public static final String GROUPTITLE = "exercise_group_title";
            public static final String STARTDATE = "exercise_start_date";
            public static final String ENDDATE = "exercise_end_date";
            public static final String NEEDTIMER = "exercise_need_timer";
            public static final String UUID = "exercise_uuid";
            public static final String REPLAYS = "exercise_replays";
            public static final String APPROACH = "exercise_approach";
            public static final String PARENTUUID = "exercise_parent_uuid";
            public static final String PARENTTRAININGDAYUUID = "exercise_parent_training_day_uuid";
            public static final String NEEDPULLCOUNTER = "exercise_need_pull_counter";
            public static final String ALREADYWORKING = "exercise_already_working";
            public static final String ALREADYENDED = "exercise_already_ended";
            public static final String RESERVEUUID = "exercise_reserve_uuid";
            public static final String PARENTDAYID = "exercise_parent_day_id";
            public static final String WEIGHT = "exercise_weight";
            public static final String ENERGY = "exercise_energy";
        }
    }
}
