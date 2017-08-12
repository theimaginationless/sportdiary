package com.app.dmitryteplyakov.sportdiary.Core.NutritionDay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.dmitryteplyakov.sportdiary.database.NutritionDay.NutritionDayBaseHelper;
import com.app.dmitryteplyakov.sportdiary.database.NutritionDay.NutritionDayCursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.NutritionDay.NutritionDayDbSchema.*;

/**
 * Created by dmitry21 on 12.08.17.
 */

public class NutritionDayStorage {
    private static NutritionDayStorage sNutritionDayStorage;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static NutritionDayStorage get(Context context) {
        if(sNutritionDayStorage == null)
            sNutritionDayStorage = new NutritionDayStorage(context);
        return sNutritionDayStorage;
    }

    private NutritionDayStorage(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new NutritionDayBaseHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues(NutritionDay nutritionDay) {
        ContentValues values = new ContentValues();
        values.put(NutritionDayTable.Cols.UUID, nutritionDay.getId().toString());
        values.put(NutritionDayTable.Cols.ASSOCIATEDDAYUUID, nutritionDay.getAssociatedDay().toString());
        values.put(NutritionDayTable.Cols.Date, nutritionDay.getDate().toString());
        values.put(NutritionDayTable.Cols.ISASSOCIATEDWITHDAY, nutritionDay.isAssociatedWithDay() ? 1 : 0);

        return values;
    }

    private NutritionDayCursorWrapper queryNutritionDays(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NutritionDayTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new NutritionDayCursorWrapper(cursor);
    }

    public List<NutritionDay> getNutritionDaysByAssociatedDayId(UUID associatedId) {
        List<NutritionDay> nutritions = new ArrayList<>();

        NutritionDayCursorWrapper cursor = queryNutritionDays(NutritionDayTable.Cols.ASSOCIATEDDAYUUID + " = ?",
                new String[] { associatedId.toString() }
        );

        cursor.moveToFirst();

        try {
            while(!cursor.isAfterLast()) {
                nutritions.add(cursor.getNutritionDay());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return nutritions;
    }

    public List<NutritionDay> getNutritionDays() {
        List<NutritionDay> nutritionDays = new ArrayList<>();

        NutritionDayCursorWrapper cursor = queryNutritionDays(null, null);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                nutritionDays.add(cursor.getNutritionDay());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return nutritionDays;
    }

    public NutritionDay getNutritionDay(UUID id) {
        NutritionDayCursorWrapper cursor = queryNutritionDays(NutritionDayTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if(cursor.getCount() == 0)  return null;
            cursor.moveToFirst();
            return cursor.getNutritionDay();
        } finally {
            cursor.close();
        }
    }

    public void addNutritionDay(NutritionDay nutrition) {
        ContentValues values = getContentValues(nutrition);
        mDatabase.insert(NutritionDayTable.NAME, null, values);
    }

    public void updateNutritionDay(NutritionDay nutrition) {
        String uuidString = nutrition.getId().toString();
        ContentValues values = getContentValues(nutrition);
        mDatabase.update(NutritionDayTable.NAME, values, NutritionDayTable.Cols.UUID + " = ?",
                new String[] { uuidString }
        );
    }

    public void deleteNutritionDay(NutritionDay nutrition) {
        mDatabase.delete(NutritionDayTable.NAME, NutritionDayTable.Cols.UUID + " = ?",
                new String[] { nutrition.getId().toString() }
        );
    }
}
