package com.app.dmitryteplyakov.sportdiary.database.Timer;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.app.dmitryteplyakov.sportdiary.Core.Timer.Timer;

import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.Timer.TimerDbSchema.*;

/**
 * Created by dmitry21 on 24.08.17.
 */

public class TimerCursorWrapper extends CursorWrapper {
    public TimerCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Timer getTimer() {
        String titleString = getString(getColumnIndex(TimerTable.Cols.TITLE));
        String uuidString = getString(getColumnIndex(TimerTable.Cols.UUID));
        String parentUuidString = getString(getColumnIndex(TimerTable.Cols.PARENTUUID));
        int iterationsInt = getInt(getColumnIndex(TimerTable.Cols.ITERATIONS));
        int preparingInt = getInt(getColumnIndex(TimerTable.Cols.PREPARING));
        int workoutInt = getInt(getColumnIndex(TimerTable.Cols.WORKOUT));
        int restInt = getInt(getColumnIndex(TimerTable.Cols.REST));
        int setsInt = getInt(getColumnIndex(TimerTable.Cols.SETS));
        int restBetweenSetsInt = getInt(getColumnIndex(TimerTable.Cols.RESTBETWEENSETS));
        int calmDownInt = getInt(getColumnIndex(TimerTable.Cols.CALMDOWN));

        Timer timer = new Timer();
        timer.setTitle(titleString);
        timer.setId(UUID.fromString(uuidString));
        timer.setParent(UUID.fromString(parentUuidString));
        timer.setIterations(iterationsInt);
        timer.setPreparing(preparingInt);
        timer.setWorkout(workoutInt);
        timer.setRest(restInt);
        timer.setRestBetweenSets(restBetweenSetsInt);
        timer.setCalmDown(calmDownInt);
        timer.setSets(setsInt);

        return timer;
    }
}
