package com.app.dmitryteplyakov.sportdiary.database.Day;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.app.dmitryteplyakov.sportdiary.database.Day.DayDbSchema.*;

/**
 * Created by Dmitry on 23.07.2017.
 */

public class DaysBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "daysBase.db";

    public DaysBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DayTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                DayTable.Cols.UUID + ", " +
                DayTable.Cols.TRAININGUUID + ", " +
                DayTable.Cols.DATE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
