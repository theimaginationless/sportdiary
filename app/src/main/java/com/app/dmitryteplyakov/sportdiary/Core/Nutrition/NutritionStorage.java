package com.app.dmitryteplyakov.sportdiary.Core.Nutrition;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.dmitryteplyakov.sportdiary.database.Nutrition.NutritionBaseHelper;
import com.app.dmitryteplyakov.sportdiary.database.Nutrition.NutritionCursorWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.Nutrition.NutritionDbSchema.*;

/**
 * Created by dmitry21 on 11.08.17.
 */

public class NutritionStorage {
    private static NutritionStorage sNutritionStorage;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static NutritionStorage get(Context context) {
        if(sNutritionStorage == null)
            sNutritionStorage = new NutritionStorage(context);
        return sNutritionStorage;
    }

    private NutritionStorage(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = NutritionBaseHelper.getInstance(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues(Nutrition nutrition) {
        ContentValues values = new ContentValues();
        values.put(NutritionTable.Cols.UUID, nutrition.getId().toString());
        values.put(NutritionTable.Cols.TITLE, nutrition.getProductTitle());
        values.put(NutritionTable.Cols.PARENTDAYUUID, nutrition.getParentDay().toString());
        values.put(NutritionTable.Cols.ENERGY, nutrition.getEnergy());
        values.put(NutritionTable.Cols.WEIGHT, nutrition.getWeight());
        values.put(NutritionTable.Cols.RESULTENERGY, nutrition.getResultEnergy());
        return values;
    }

    private NutritionCursorWrapper queryNutritions(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NutritionTable.NAME,
                null, // Columns - null перебирает все столбцы
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new NutritionCursorWrapper(cursor);
    }

    public List<Nutrition> getNutritions() {
        List<Nutrition> nutritions = new ArrayList<>();

        NutritionCursorWrapper cursor = queryNutritions(null, null);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                nutritions.add(cursor.getNutrition());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Collections.reverse(nutritions);
        return nutritions;
    }

    public List<Nutrition> getNutritionsByParentDayId(UUID parentId) {
        List<Nutrition> nutritions = new ArrayList<>();
        NutritionCursorWrapper cursor = queryNutritions(NutritionTable.Cols.PARENTDAYUUID + " = ?",
                new String[] { parentId.toString() }
        );

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                nutritions.add(cursor.getNutrition());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Collections.reverse(nutritions);
        return nutritions;
    }

    public Nutrition getNutrition(UUID id) {
        NutritionCursorWrapper cursor = queryNutritions(NutritionTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if(cursor.getCount() == 0)  return null;
            cursor.moveToFirst();
            return cursor.getNutrition();
        } finally {
            cursor.close();
        }
    }

    public void addNutrition(Nutrition nutrition) {
        ContentValues values = getContentValues(nutrition);
        mDatabase.insert(NutritionTable.NAME, null, values);
    }

    public void updateNutrition(Nutrition nutrition) {
        String uuidString = nutrition.getId().toString();
        ContentValues values = getContentValues(nutrition);
        mDatabase.update(NutritionTable.NAME, values, NutritionTable.Cols.UUID + " = ?",
                new String[] { uuidString }
        );
    }

    public void deleteNutrition(Nutrition nutrition) {
        mDatabase.delete(NutritionTable.NAME, NutritionTable.Cols.UUID + " = ?",
                new String[] { nutrition.getId().toString() }
        );
    }

    public void deleteNutritionsByParentDayId(UUID parentId) {
        mDatabase.delete(NutritionTable.NAME, NutritionTable.Cols.PARENTDAYUUID + " = ?",
                new String[] { parentId.toString() }
        );
    }
}
