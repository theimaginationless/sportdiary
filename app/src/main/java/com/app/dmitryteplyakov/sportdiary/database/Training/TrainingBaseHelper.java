package com.app.dmitryteplyakov.sportdiary.database.Training;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.app.dmitryteplyakov.sportdiary.database.Training.TrainingDbSchema.*;

/**
 * Created by Dmitry on 24.07.2017.
 */

public class TrainingBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "trainingBase.db";
    private static TrainingBaseHelper mInstance = null;

    public static TrainingBaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TrainingBaseHelper(context);
        }
        return mInstance;
    }

    private TrainingBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TrainingTable.NAME + "(" +
        " _id integer primary key autoincrement, " +
        TrainingTable.Cols.TITLE + ", " +
        TrainingTable.Cols.UUID + ", " +
        TrainingTable.Cols.PARENTDAYUUID + ", " +
        TrainingTable.Cols.PARENTUUID +
        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
