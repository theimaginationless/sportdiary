package com.app.dmitryteplyakov.sportdiary.database.NutritionDay;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.app.dmitryteplyakov.sportdiary.database.NutritionDay.NutritionDayDbSchema.*;

/**
 * Created by dmitry21 on 12.08.17.
 */

public class NutritionDayBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "nutritionsDayBase.db";

    public NutritionDayBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NutritionDayTable.NAME + "(" +
        " _id integer primary key autoincrement, " +
                NutritionDayTable.Cols.UUID + ", " +
                NutritionDayTable.Cols.ASSOCIATEDDAYUUID + ", " +
                NutritionDayTable.Cols.Date + ", " +
                NutritionDayTable.Cols.ISASSOCIATEDWITHDAY +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
