package com.app.dmitryteplyakov.sportdiary.Core.Timer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.dmitryteplyakov.sportdiary.database.Timer.TimerBaseHelper;
import com.app.dmitryteplyakov.sportdiary.database.Timer.TimerCursorWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.Timer.TimerDbSchema.*;

/**
 * Created by dmitry21 on 20.08.17.
 */

public class TimerStorage {
    private static TimerStorage sTimerStorage;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static TimerStorage get(Context context) {
        if(sTimerStorage == null)
            sTimerStorage = new TimerStorage(context);
        return sTimerStorage;
    }

    private TimerStorage(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TimerBaseHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues(Timer timer) {
        ContentValues values = new ContentValues();
        values.put(TimerTable.Cols.TITLE, timer.getTitle());
        values.put(TimerTable.Cols.UUID, timer.getId().toString());
        values.put(TimerTable.Cols.PARENTUUID, timer.getParent().toString());
        values.put(TimerTable.Cols.ITERATIONS, timer.getIterations());
        values.put(TimerTable.Cols.PREPARING, timer.getPreparing());
        values.put(TimerTable.Cols.WORKOUT, timer.getWorkout());
        values.put(TimerTable.Cols.REST, timer.getRest());
        values.put(TimerTable.Cols.SETS, timer.getSets());
        values.put(TimerTable.Cols.RESTBETWEENSETS, timer.getRestBetweenSets());
        values.put(TimerTable.Cols.CALMDOWN, timer.getCalmDown());

        return values;
    }

    private TimerCursorWrapper queryTimer(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TimerTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new TimerCursorWrapper(cursor);
    }

    public List<Timer> getTimers() {
        List<Timer> timers = new ArrayList<>();
        TimerCursorWrapper cursor = queryTimer(null, null);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                timers.add(cursor.getTimer());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Collections.reverse(timers);
        return timers;
    }

    public List<Timer> getTimersByParentId(UUID id) {
        List<Timer> timers = new ArrayList<>();

        TimerCursorWrapper cursor = queryTimer(TimerTable.Cols.PARENTUUID + " = ?",
                new String[] {id.toString()}
        );
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                timers.add(cursor.getTimer());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Collections.reverse(timers);
        return timers;
    }

    public Timer getTimer(UUID id) {
        TimerCursorWrapper cursor = queryTimer(TimerTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            cursor.moveToFirst();
            if(cursor.getCount() == 0) return null;
            return cursor.getTimer();
        } finally {
            cursor.close();
        }
    }

    public void addTimer(Timer timer) {
        ContentValues values = getContentValues(timer);
        mDatabase.insert(TimerTable.NAME, null, values);
    }

    public void updateTimer(Timer timer) {
        ContentValues values = getContentValues(timer);
        mDatabase.update(TimerTable.NAME, values, TimerTable.Cols.UUID + " = ?",
                new String[]{timer.getId().toString()}
        );
    }

    public void deleteTimer(Timer timer) {
        mDatabase.delete(TimerTable.NAME, TimerTable.Cols.UUID + " = ?",
                new String[]{timer.getId().toString()}
        );
    }

    public void deleteTimersByParentId(UUID id) {
        mDatabase.delete(TimerTable.NAME, TimerTable.Cols.PARENTUUID + " = ?",
                new String[]{id.toString()}
        );
    }
}
