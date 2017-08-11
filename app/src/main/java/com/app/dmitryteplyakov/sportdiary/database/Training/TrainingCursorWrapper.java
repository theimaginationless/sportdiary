package com.app.dmitryteplyakov.sportdiary.database.Training;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.app.dmitryteplyakov.sportdiary.Core.Training.Training;

import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.Training.TrainingDbSchema.*;

/**
 * Created by Dmitry on 24.07.2017.
 */

public class TrainingCursorWrapper extends CursorWrapper {
    public TrainingCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Training getTraining() {
        String uuidString = getString(getColumnIndex(TrainingTable.Cols.UUID));
        String parentUuidString = getString(getColumnIndex(TrainingTable.Cols.PARENTUUID));
        String parentDayUuidString = getString(getColumnIndex(TrainingTable.Cols.PARENTDAYUUID));
        String title = getString(getColumnIndex(TrainingTable.Cols.TITLE));

        Training training = new Training(UUID.fromString(uuidString), UUID.fromString(parentUuidString));
        Log.d("DB", "TrainingDayId: " + parentDayUuidString);
        Log.d("DB", "TrainingId: " + uuidString);
        training.setParentDayId(UUID.fromString(parentDayUuidString));
        training.setTitle(title);
        return training;
    }
}
