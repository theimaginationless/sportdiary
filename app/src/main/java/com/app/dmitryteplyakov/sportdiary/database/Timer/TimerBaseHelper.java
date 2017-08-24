package com.app.dmitryteplyakov.sportdiary.database.Timer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.app.dmitryteplyakov.sportdiary.database.Timer.TimerDbSchema.*;

/**
 * Created by dmitry21 on 24.08.17.
 */

public class TimerBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "timersBase.db";

    public TimerBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TimerTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                TimerTable.Cols.TITLE + ", " +
                TimerTable.Cols.UUID + ", " +
                TimerTable.Cols.CONNECTEDWITH + ", " +
                TimerTable.Cols.ITERATIONS + ", " +
                TimerTable.Cols.PREPARING + ", " +
                TimerTable.Cols.WORKOUT + ", " +
                TimerTable.Cols.REST + ", " +
                TimerTable.Cols.SETS + ", " +
                TimerTable.Cols.RESTBETWEENSETS + ", " +
                TimerTable.Cols.CALMDOWN + ", " +
                TimerTable.Cols.ISCONNECTED +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
