package com.app.dmitryteplyakov.sportdiary.database.Exercise;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.app.dmitryteplyakov.sportdiary.database.Exercise.ExerciseDbSchema.*;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class CompExerciseBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "CompExercisesBase.db";

    public CompExerciseBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ExerciseTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            ExerciseTable.Cols.UUID + ", " +
            ExerciseTable.Cols.PARENTUUID + ", " +
            ExerciseTable.Cols.PARENTTRAININGDAYUUID + ", " +
            ExerciseTable.Cols.GROUPTITLE + ", " +
            ExerciseTable.Cols.TITLE + ", " +
            ExerciseTable.Cols.STARTDATE + ", " +
            ExerciseTable.Cols.ENDDATE + ", " +
            ExerciseTable.Cols.REPLAYS + ", " +
            ExerciseTable.Cols.APPROACH + ", " +
            ExerciseTable.Cols.NEEDPULLCOUNTER + ", " +
            ExerciseTable.Cols.ALREADYWORKING + ", " +
            ExerciseTable.Cols.ALREADYENDED + ", " +
            ExerciseTable.Cols.RESERVEUUID + ", " +
            ExerciseTable.Cols.PARENTDAYID + ", " +
            ExerciseTable.Cols.WEIGHT + ", " +
            ExerciseTable.Cols.ENERGY + ", " +
            ExerciseTable.Cols.NEEDTIMER +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
