package com.app.dmitryteplyakov.sportdiary.database.Nutrition;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.dmitryteplyakov.sportdiary.database.Exercise.ExerciseBaseHelper;

import static com.app.dmitryteplyakov.sportdiary.database.Nutrition.NutritionDbSchema.*;

/**
 * Created by dmitry21 on 11.08.17.
 */

public class NutritionBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "nutritionsBase.db";
    private static NutritionBaseHelper mInstance = null;

    public static NutritionBaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NutritionBaseHelper(context);
        }
        return mInstance;
    }


    private NutritionBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NutritionTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                NutritionTable.Cols.TITLE + ", " +
                NutritionTable.Cols.UUID + ", " +
                NutritionTable.Cols.PARENTDAYUUID + ", " +
                NutritionTable.Cols.ENERGY + ", " +
                NutritionTable.Cols.WEIGHT + ", " +
                NutritionTable.Cols.RESULTENERGY +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
