package com.app.dmitryteplyakov.sportdiary.database.Weight;

/**
 * Created by dmitry21 on 22.08.17.
 */

public class WeightDbSchema {
    public static final class WeightTable {
        public static final String NAME = "weight_title";
        public static final class Cols {
            public static final String DATE = "weight_date";
            public static final String UUID = "weight_uuid";
            public static final String VALUE = "weight_value";
        }
    }
}
