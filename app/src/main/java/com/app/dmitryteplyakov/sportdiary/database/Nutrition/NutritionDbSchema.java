package com.app.dmitryteplyakov.sportdiary.database.Nutrition;

/**
 * Created by dmitry21 on 11.08.17.
 */

public class NutritionDbSchema {
    public static final class NutritionTable {
        public static final String NAME = "nutritions";

        public static final class Cols {
            public static final String TITLE = "nutrition_title";
            public static final String UUID = "nutrition_uuid";
            public static final String PARENTDAYUUID = "nutrition_parent_uuid";
            public static final String ENERGY = "nutrition_energy";
            public static final String WEIGHT = "nutrition_weight";
        }
    }
}
