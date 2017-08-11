package com.app.dmitryteplyakov.sportdiary.Core.Training;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.dmitryteplyakov.sportdiary.database.Training.CompTrainingBaseHelper;
import com.app.dmitryteplyakov.sportdiary.database.Training.TrainingBaseHelper;
import com.app.dmitryteplyakov.sportdiary.database.Training.TrainingCursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.Training.TrainingDbSchema.TrainingTable;

/**
 * Created by Dmitry on 24.07.2017.
 */

public class CompTrainingStorage {
    private static CompTrainingStorage sTrainingStorage;
    private List<Training> mTrainings;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CompTrainingStorage(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CompTrainingBaseHelper(mContext).getWritableDatabase();
    }

    public static CompTrainingStorage get(Context context) {
        if(sTrainingStorage == null)
            sTrainingStorage = new CompTrainingStorage(context);
        return sTrainingStorage;
    }

    private static ContentValues getContentValues(Training training) {
        ContentValues values = new ContentValues();
        values.put(TrainingTable.Cols.UUID, training.getId().toString());
        values.put(TrainingTable.Cols.PARENTUUID, training.getParentId().toString());
        values.put(TrainingTable.Cols.PARENTDAYUUID, training.getParentDayId().toString());
        values.put(TrainingTable.Cols.TITLE, training.getTitle());
        return values;
    }

    private TrainingCursorWrapper queryTraining(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TrainingTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new TrainingCursorWrapper(cursor);
    }

    public List<Training> getTrainings() {
        List<Training> trainings = new ArrayList<>();
        TrainingCursorWrapper cursor = queryTraining(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                trainings.add(cursor.getTraining());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return trainings;
    }

    public static CompTrainingStorage getTrainingStorage() {
        return sTrainingStorage;
    }

    public static void setTrainingStorage(CompTrainingStorage trainingStorage) {
        sTrainingStorage = trainingStorage;
    }

    public void updateTraining(Training training) {
        String uuidString = training.getId().toString();
        ContentValues values = getContentValues(training);

        mDatabase.update(TrainingTable.NAME, values, TrainingTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public void addTraining(Training training) {
        ContentValues values = getContentValues(training);
        mDatabase.insert(TrainingTable.NAME, null, values);
    }

    public Training getTraining(UUID id) {
        TrainingCursorWrapper cursor = queryTraining(TrainingTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if(cursor.getCount() == 0)  return null;
            cursor.moveToFirst();
            return cursor.getTraining();
        } finally {
            cursor.close();
        }
    }

    public void deleteTraining(UUID id) {
        mDatabase.delete(TrainingTable.NAME, TrainingTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
    }

    public void deleteTrainingByDayId(UUID id) {
        mDatabase.delete(TrainingTable.NAME, TrainingTable.Cols.PARENTDAYUUID + " = ?",
                new String[] { id.toString() }
        );
    }

    public Training getTrainingByParentDayId(UUID id) {
        TrainingCursorWrapper cursor = queryTraining(TrainingTable.Cols.PARENTDAYUUID + " = ?",
                new String[] { id.toString() }
        );
        Log.d("CORE CTS", "DAY ID: " + id.toString());

        try {
            if(cursor.getCount() == 0)  return null;
            cursor.moveToFirst();
            Log.d("CORE CTS", "TRAINING ID: " + cursor.getTraining().getId().toString());
            return cursor.getTraining();
        } finally {
            cursor.close();
        }
    }
}
