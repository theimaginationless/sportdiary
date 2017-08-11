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
        String associatedDayUuid = getString(getColumnIndex(NutritionTable.Cols.ASSOCIATEDDAYUUID));
        int energy = getInt(getColumnIndex(NutritionTable.Cols.ENERGY));
        double weight = getDouble(getColumnIndex(NutritionTable.Cols.WEIGHT));
        int isAssociatedWithDay = getInt(getColumnIndex(NutritionTable.Cols.ISASSOCIATEDWITHDAY));

        Nutrition nutrition = new Nutrition(UUID.fromString(uuidString), UUID.fromString(parentDayUuid));
        nutrition.setProductTitle(title);
        nutrition.setAssociatedWithDay(isAssociatedWithDay == 0);
        nutrition.setAssociatedDay(UUID.fromString(associatedDayUuid));
        nutrition.setEnergy(energy);
        nutrition.setWeight(weight);
        return nutrition;
    }

}
