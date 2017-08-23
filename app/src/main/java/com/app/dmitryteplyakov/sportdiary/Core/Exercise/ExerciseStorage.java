package com.app.dmitryteplyakov.sportdiary.Core.Exercise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.dmitryteplyakov.sportdiary.database.Exercise.ExerciseBaseHelper;
import com.app.dmitryteplyakov.sportdiary.database.Exercise.ExerciseCursorWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.Exercise.ExerciseDbSchema.*;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class ExerciseStorage {
    private static ExerciseStorage sExerciseStorage;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private UUID parentUUID;

    public static ExerciseStorage get(Context context) {
        if(sExerciseStorage == null)
            sExerciseStorage = new ExerciseStorage(context);
        return sExerciseStorage;
    }

    private ExerciseStorage(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ExerciseBaseHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues(Exercise exercise) {
        ContentValues values = new ContentValues();
        values.put(ExerciseTable.Cols.UUID, exercise.getId().toString());
        values.put(ExerciseTable.Cols.GROUPTITLE, exercise.getGroupTitle());
        values.put(ExerciseTable.Cols.PARENTUUID, exercise.getParentUUID().toString());
        values.put(ExerciseTable.Cols.PARENTTRAININGDAYUUID, exercise.getParentTrainingDayId().toString());
        values.put(ExerciseTable.Cols.TITLE, exercise.getTitle());
        values.put(ExerciseTable.Cols.STARTDATE, exercise.getStartDate().getTime());
        values.put(ExerciseTable.Cols.ENDDATE, exercise.getEndDate().getTime());
        values.put(ExerciseTable.Cols.REPLAYS, Integer.toString(exercise.getReplays()));
        values.put(ExerciseTable.Cols.APPROACH, Integer.toString(exercise.getApproach()));
        values.put(ExerciseTable.Cols.NEEDTIMER, exercise.isNeedTimer() ? 1 : 0);
        values.put(ExerciseTable.Cols.NEEDPULLCOUNTER, exercise.isNeedPullCounter() ? 1 : 0);
        values.put(ExerciseTable.Cols.ALREADYWORKING, exercise.isAlreadyWorking() ? 1 : 0);
        values.put(ExerciseTable.Cols.ALREADYENDED, exercise.isAlreadyEnded() ? 1 : 0);
        values.put(ExerciseTable.Cols.RESERVEUUID, exercise.getReserveId().toString());
        values.put(ExerciseTable.Cols.PARENTDAYID, exercise.getParentDayId().toString());
        values.put(ExerciseTable.Cols.WEIGHT, exercise.getWeight());
        values.put(ExerciseTable.Cols.ENERGY, exercise.getEnergy());

        return values;
    }

    private ExerciseCursorWrapper queryExercises(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ExerciseTable.NAME,
                null, // Columns - null перебирает все столбцы
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ExerciseCursorWrapper(cursor);
    }

    public List<Exercise> getExercises() {
        List<Exercise> exercises = new ArrayList<>();

        ExerciseCursorWrapper cursor = queryExercises(null, null);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                exercises.add(cursor.getExercise());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Collections.reverse(exercises);
        return exercises;
    }

    public List<Exercise> getExercisesByParentId(UUID id) {
        List<Exercise> exercises = new ArrayList<>();

        ExerciseCursorWrapper cursor = queryExercises(ExerciseTable.Cols.PARENTUUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                exercises.add(cursor.getExercise());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Collections.reverse(exercises);
        return exercises;
    }

    public Exercise getExercise(UUID id) {
        ExerciseCursorWrapper cursor = queryExercises(ExerciseTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if(cursor.getCount() == 0)  return null;
            cursor.moveToFirst();
            return cursor.getExercise();
        } finally {
            cursor.close();
        }
    }

    public void addExercise(Exercise exercise) {
        ContentValues values = getContentValues(exercise);
        mDatabase.insert(ExerciseTable.NAME, null, values);
    }

    public void updateExercise(Exercise exercise) {
        String uuidString = exercise.getId().toString();
        ContentValues values = getContentValues(exercise);

        mDatabase.update(ExerciseTable.NAME, values, ExerciseTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public void deleteExercise(Exercise exercise) {
        mDatabase.delete(ExerciseTable.NAME, ExerciseTable.Cols.UUID + " = ?",
                new String[] { exercise.getId().toString() }
        );
    }

    public void deleteExercisesByParentId(UUID parentId) {
        mDatabase.delete(ExerciseTable.NAME, ExerciseTable.Cols.PARENTUUID + " = ?",
                new String[] { parentId.toString() }
        );
    }

    public void deleteExerciseById(UUID id) {
        mDatabase.delete(ExerciseTable.NAME, ExerciseTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
    }


}
