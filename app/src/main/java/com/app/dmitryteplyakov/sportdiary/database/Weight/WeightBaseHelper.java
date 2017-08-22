package com.app.dmitryteplyakov.sportdiary.database.Weight;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.app.dmitryteplyakov.sportdiary.database.Weight.WeightDbSchema.*;

/**
 * Created by dmitry21 on 22.08.17.
 */

public class WeightBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "weightBase.db";

    public WeightBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + WeightTable.NAME + "(" +
        " _id integer primary key autoincrement, " +
        WeightTable.Cols.UUID + ", " +
        WeightTable.Cols.DATE + ", " +
        WeightTable.Cols.VALUE +
        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
