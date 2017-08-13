package com.app.dmitryteplyakov.sportdiary.database.NutritionDay;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.app.dmitryteplyakov.sportdiary.Core.NutritionDay.NutritionDay;

import java.util.Date;
import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.NutritionDay.NutritionDayDbSchema.*;

/**
 * Created by dmitry21 on 12.08.17.
 */

public class NutritionDayCursorWrapper extends CursorWrapper {
    public NutritionDayCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public NutritionDay getNutritionDay() {
        String uuidString = getString(getColumnIndex(NutritionDayTable.Cols.UUID));
        String associatedDayUuidString = getString(getColumnIndex(NutritionDayTable.Cols.ASSOCIATEDDAYUUID));
        long dateLong = getLong(getColumnIndex(NutritionDayTable.Cols.Date));
        int isAssociatedWithDay = getInt(getColumnIndex(NutritionDayTable.Cols.ISASSOCIATEDWITHDAY));

        NutritionDay nutritionDay = new NutritionDay(UUID.fromString(uuidString));
        nutritionDay.setAssociatedDay(UUID.fromString(associatedDayUuidString));
        nutritionDay.setDate(new Date(dateLong));
        nutritionDay.setAssociatedWithDay(isAssociatedWithDay != 0);
        return nutritionDay;
    }
}
