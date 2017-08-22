package com.app.dmitryteplyakov.sportdiary.database.Weight;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.app.dmitryteplyakov.sportdiary.Core.Weight.Weight;

import java.util.Date;
import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.Weight.WeightDbSchema.*;

/**
 * Created by dmitry21 on 22.08.17.
 */

public class WeightCursorWrapper extends CursorWrapper {
    public WeightCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Weight getWeight() {
        String uuidString = getString(getColumnIndex(WeightTable.Cols.UUID));
        long dateLong = getLong(getColumnIndex(WeightTable.Cols.DATE));
        float valueFloat = getFloat(getColumnIndex(WeightTable.Cols.VALUE));

        Weight weight = new Weight(valueFloat);
        weight.setDate(new Date(dateLong));
        weight.setId(UUID.fromString(uuidString));
        return weight;
    }
}
