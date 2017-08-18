package com.app.dmitryteplyakov.sportdiary.Core.Day;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.app.dmitryteplyakov.sportdiary.database.Day.DayCursorWrapper;
import com.app.dmitryteplyakov.sportdiary.database.Day.DaysBaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.Day.DayDbSchema.*;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class DayStorage {
    private static DayStorage sDayStorage;
    private List<Day> mDayStorages;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DayStorage get(Context context) {
        if(sDayStorage == null)
            sDayStorage = new DayStorage(context);
        return sDayStorage;
    }

    private DayStorage(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DaysBaseHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues(Day day) {
        ContentValues values = new ContentValues();
        values.put(DayTable.Cols.UUID, day.getId().toString());
        values.put(DayTable.Cols.DATE, day.getDate().getTime());
        values.put(DayTable.Cols.TRAININGUUID, day.getTrainingId().toString());

        return values;
    }

    private DayCursorWrapper queryDays(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DayTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new DayCursorWrapper(cursor);
    }

    public List<Day> getDays() {
        List<Day> days = new ArrayList<>();

        DayCursorWrapper cursor = queryDays(null, null);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                days.add(cursor.getDay());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Collections.sort(days);
        return days;
    }

    public static void setDayStorage(DayStorage dayStorage) {
        sDayStorage = dayStorage;
    }

    public void addDay(Day d) {
        ContentValues values = getContentValues(d);
        mDatabase.insert(DayTable.NAME, null, values);
    }

    public void updateDay(Day day) {
        String uuidString = day.getId().toString();
        ContentValues values = getContentValues(day);

        mDatabase.update(DayTable.NAME, values, DayTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public Day getDay(UUID id) {
        DayCursorWrapper cursor = queryDays(DayTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if(cursor.getCount() == 0)  return null;
            cursor.moveToFirst();
            return cursor.getDay();
        } finally {
            cursor.close();
        }
    }

    public void deleteDay(Day day) {
        mDatabase.delete(DayTable.NAME, DayTable.Cols.UUID + " = ?",
                new String[] { day.getId().toString() }
        );
    }

    public void deleteDayByTrainingId(UUID trainingId) {
        mDatabase.delete(DayTable.NAME, DayTable.Cols.TRAININGUUID + " = ?",
                new String[] { trainingId.toString() }
        );
    }

    public List<Day> getDaysByTrainingId(UUID trainingId) {
        List<Day> days = new ArrayList<>();
        DayCursorWrapper cursor = queryDays(DayTable.Cols.TRAININGUUID + " = ?",
                new String[] { trainingId.toString() }
        );

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                days.add(cursor.getDay());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return days;
    }

    public Day getDayByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date currDate = calendar.getTime();
        List<Day> days = new ArrayList<>();

        DayCursorWrapper cursor = queryDays(null, null);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                days.add(cursor.getDay());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        for(Day lDay : days)
            if(lDay.getDate().equals(currDate))
                return lDay;
        return null;
    }
}
