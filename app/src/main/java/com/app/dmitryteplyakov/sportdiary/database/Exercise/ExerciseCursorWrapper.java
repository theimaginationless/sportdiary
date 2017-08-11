package com.app.dmitryteplyakov.sportdiary.database.Exercise;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.app.dmitryteplyakov.sportdiary.Core.Exercise.Exercise;

import java.util.Date;
import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.Exercise.ExerciseDbSchema.*;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class ExerciseCursorWrapper extends CursorWrapper {
    public ExerciseCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Exercise getExercise() {
        String uuidString = getString(getColumnIndex(ExerciseTable.Cols.UUID));
        String parentUuidString = getString(getColumnIndex(ExerciseTable.Cols.PARENTUUID));
        String parentTrainingDayUuid = getString(getColumnIndex(ExerciseTable.Cols.PARENTTRAININGDAYUUID));
        String reserveUuid = getString(getColumnIndex(ExerciseTable.Cols.RESERVEUUID));
        String parentDayUuid = getString(getColumnIndex(ExerciseTable.Cols.PARENTDAYID));
        String groupTitle = getString(getColumnIndex(ExerciseTable.Cols.GROUPTITLE));
        String title = getString(getColumnIndex(ExerciseTable.Cols.TITLE));
        long startDate = getLong(getColumnIndex(ExerciseTable.Cols.STARTDATE));
        long endDate = getLong(getColumnIndex(ExerciseTable.Cols.ENDDATE));
        int replays = getInt(getColumnIndex(ExerciseTable.Cols.REPLAYS));
        int approach = getInt(getColumnIndex(ExerciseTable.Cols.APPROACH));
        int needTimer = getInt(getColumnIndex(ExerciseTable.Cols.NEEDTIMER));
        int needPullCounter = getInt(getColumnIndex(ExerciseTable.Cols.NEEDPULLCOUNTER));
        int alreadyWorking = getInt(getColumnIndex(ExerciseTable.Cols.ALREADYWORKING));
        int alreadyEnded = getInt(getColumnIndex(ExerciseTable.Cols.ALREADYENDED));
        double weight = getDouble(getColumnIndex(ExerciseTable.Cols.WEIGHT));
        int energy = getInt(getColumnIndex(ExerciseTable.Cols.ENERGY));

        Exercise exercise = new Exercise(UUID.fromString(uuidString), UUID.fromString(parentUuidString));
        exercise.setParentTrainingDayId(UUID.fromString(parentTrainingDayUuid));
        exercise.setReserveId(UUID.fromString(reserveUuid));
        exercise.setParentDayId(UUID.fromString(parentDayUuid));
        exercise.setTitle(title);
        exercise.setGroupTitle(groupTitle);
        exercise.setStartDate(new Date(startDate));
        exercise.setEndDate(new Date(endDate));
        exercise.setReplays(replays);
        exercise.setNeedPullCounter(needPullCounter != 0);
        exercise.setApproach(approach);
        exercise.setWeight(weight);
        exercise.setNeedTimer(needTimer != 0);
        exercise.setAlreadyWorking(alreadyWorking != 0);
        exercise.setAlreadyEnded(alreadyEnded != 0);
        exercise.setEnergy(energy);
        Log.d("DB", "ALREADYENDED: " + Boolean.toString(alreadyEnded != 0));
        Log.d("DB", "Timer: " + Boolean.toString(exercise.isNeedTimer()));
        return exercise;
    }
}
