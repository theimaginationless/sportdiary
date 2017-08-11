package com.app.dmitryteplyakov.sportdiary.database.Day;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.app.dmitryteplyakov.sportdiary.Core.Day.Day;

import java.util.Date;
import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.Day.DayDbSchema.*;

/**
 * Created by Dmitry on 23.07.2017.
 */

public class DayCursorWrapper extends CursorWrapper {
    public DayCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Day getDay() {
        String uuidString = getString(getColumnIndex(DayTable.Cols.UUID));
        String trainingUuid = getString(getColumnIndex(DayTable.Cols.TRAININGUUID));
        long date = getLong(getColumnIndex(DayTable.Cols.DATE));

        Day day = new Day(UUID.fromString(uuidString));
        day.setDate(new Date(date));
        day.setTrainingId(UUID.fromString(trainingUuid));
        return day;
    }
}
