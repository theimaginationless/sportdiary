package com.app.dmitryteplyakov.sportdiary.database.NutritionDay;

/**
 * Created by dmitry21 on 12.08.17.
 */

public class NutritionDayDbSchema {
    public static final class NutritionDayTable {
        public static final String NAME = "nutrition_day";
        public static final class Cols {
            public static final String UUID = "nutrition_day_uuid";
            public static final String ASSOCIATEDDAYUUID = "nutrition_associated_day_uuid";
            public static final String Date = "nutrition_day_date";
            public static final String ISASSOCIATEDWITHDAY = "nutrition_day_is_associated_with_day";
        }
    }
}
