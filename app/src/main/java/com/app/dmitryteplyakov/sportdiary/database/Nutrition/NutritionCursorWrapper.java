package com.app.dmitryteplyakov.sportdiary.database.Nutrition;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.Nutrition;

import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.Nutrition.NutritionDbSchema.*;

/**
 * Created by dmitry21 on 11.08.17.
 */

public class NutritionCursorWrapper extends CursorWrapper {
    public NutritionCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Nutrition getNutrition() {
        String title = getString(getColumnIndex(NutritionTable.Cols.TITLE));
        String uuidString = getString(getColumnIndex(NutritionTable.Cols.UUID));
        String parentDayUuid = getString(getColumnIndex(NutritionTable.Cols.PARENTDAYUUID));
        int energy = getInt(getColumnIndex(NutritionTable.Cols.ENERGY));
        double weight = getDouble(getColumnIndex(NutritionTable.Cols.WEIGHT));

        Nutrition nutrition = new Nutrition(UUID.fromString(uuidString), UUID.fromString(parentDayUuid));
        nutrition.setProductTitle(title);
        nutrition.setEnergy(energy);
        nutrition.setWeight(weight);
        return nutrition;
    }

}
