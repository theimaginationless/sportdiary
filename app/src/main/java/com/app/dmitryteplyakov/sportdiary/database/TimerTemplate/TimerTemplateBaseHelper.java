package com.app.dmitryteplyakov.sportdiary.database.TimerTemplate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import static com.app.dmitryteplyakov.sportdiary.database.TimerTemplate.TimerTemplateDbSchema.*;

/**
 * Created by dmitry21 on 24.08.17.
 */

public class TimerTemplateBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "timerTemplatesBase.db";
    private static TimerTemplateBaseHelper mInstance = null;

    public static TimerTemplateBaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TimerTemplateBaseHelper(context);
        }
        return mInstance;
    }

    private TimerTemplateBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TimerTemplateTable.NAME + "(" +
        " _id integer primary key autoincrement, " +
        TimerTemplateTable.Cols.TITLE + ", " +
        TimerTemplateTable.Cols.UUID +
        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
