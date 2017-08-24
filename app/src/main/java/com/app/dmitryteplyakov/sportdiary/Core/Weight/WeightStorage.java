package com.app.dmitryteplyakov.sportdiary.Core.Weight;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.dmitryteplyakov.sportdiary.database.Weight.WeightBaseHelper;
import com.app.dmitryteplyakov.sportdiary.database.Weight.WeightCursorWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.Weight.WeightDbSchema.*;

/**
 * Created by dmitry21 on 22.08.17.
 */

public class WeightStorage {
    private static WeightStorage sStorage;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static WeightStorage get(Context context) {
        if(sStorage == null)
            sStorage = new WeightStorage(context);
        return sStorage;
    }

    private WeightStorage(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new WeightBaseHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues(Weight weight) {
        ContentValues values = new ContentValues();
        values.put(WeightTable.Cols.UUID, weight.getId().toString());
        values.put(WeightTable.Cols.DATE, weight.getDate().getTime());
        values.put(WeightTable.Cols.VALUE, weight.getValue());

        return values;
    }

    private WeightCursorWrapper queryWeight(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                WeightTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new WeightCursorWrapper(cursor);
    }

    public List<Weight> getWeights() {
        List<Weight> weights = new ArrayList<>();

        WeightCursorWrapper cursor = queryWeight(null, null);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                weights.add(cursor.getWeight());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Collections.sort(weights);
        return weights;
    }

    public List<Weight> getWeightsFromDayRange(Date date) {
        List<Weight> weights = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long startBound = calendar.getTime().getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        long endBound = calendar.getTime().getTime();
        WeightCursorWrapper cursor = queryWeight("CAST(" + WeightTable.Cols.DATE + " AS TEXT) BETWEEN " + "CAST(? AS TEXT) AND " + "CAST(? AS TEXT)",
                new String[] { Long.toString(startBound), Long.toString(endBound) }
        );

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                weights.add(cursor.getWeight());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        if(cursor.getCount() == 0) return null;
        Collections.sort(weights);
        return weights;
    }

    public Weight getWeight(UUID id) {
        WeightCursorWrapper cursor = queryWeight(WeightTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            cursor.moveToFirst();
            if(cursor.getCount() == 0) return null;
            return cursor.getWeight();
        } finally {
            cursor.close();
        }
    }

    public void addWeight(Weight weight) {
        ContentValues values = getContentValues(weight);
        mDatabase.insert(WeightTable.NAME, null, values);
    }

    public void updateWeight(Weight weight) {
        ContentValues values = getContentValues(weight);
        mDatabase.update(WeightTable.NAME, values, WeightTable.Cols.UUID + " = ?",
                new String[] { weight.getId().toString() }
        );
    }

    public void deleteWeight(Weight weight) {
        mDatabase.delete(WeightTable.NAME, WeightTable.Cols.UUID + " = ?",
                new String[] { weight.getId().toString() }
        );
    }
}
