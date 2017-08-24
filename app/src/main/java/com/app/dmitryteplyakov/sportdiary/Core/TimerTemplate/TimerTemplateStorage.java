package com.app.dmitryteplyakov.sportdiary.Core.TimerTemplate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.dmitryteplyakov.sportdiary.database.TimerTemplate.TimerTemplateBaseHelper;
import com.app.dmitryteplyakov.sportdiary.database.TimerTemplate.TimerTemplateCursorWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.TimerTemplate.TimerTemplateDbSchema.*;

/**
 * Created by dmitry21 on 24.08.17.
 */

public class TimerTemplateStorage {
    private static TimerTemplateStorage sTimerTemplateStorage;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static TimerTemplateStorage get(Context context) {
        if(sTimerTemplateStorage == null)
            sTimerTemplateStorage = new TimerTemplateStorage(context);
        return sTimerTemplateStorage;
    }

    private TimerTemplateStorage(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TimerTemplateBaseHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues(TimerTemplate template) {
        ContentValues values = new ContentValues();
        values.put(TimerTemplateTable.Cols.TITLE, template.getTitle());
        values.put(TimerTemplateTable.Cols.UUID, template.getId().toString());
        return values;
    }

    private TimerTemplateCursorWrapper queryTimerTemplate(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TimerTemplateTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new TimerTemplateCursorWrapper(cursor);
    }

    public List<TimerTemplate> getTemplates() {
        List<TimerTemplate> templates = new ArrayList<>();
        TimerTemplateCursorWrapper cursor = queryTimerTemplate(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                templates.add(cursor.getTemplate());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        Collections.reverse(templates);
        return templates;
    }

    public TimerTemplate getTemplate(UUID id) {
        TimerTemplateCursorWrapper cursor = queryTimerTemplate(TimerTemplateTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            cursor.moveToFirst();
            if(cursor.getCount() == 0) return null;
            return cursor.getTemplate();
        } finally {
            cursor.close();
        }
    }

    public void addTemplate(TimerTemplate template) {
        ContentValues values = getContentValues(template);
        mDatabase.insert(TimerTemplateTable.NAME, null, values);
    }

    public void updateTemplate(TimerTemplate template) {
        ContentValues values = getContentValues(template);
        mDatabase.update(TimerTemplateTable.NAME, values, TimerTemplateTable.Cols.UUID + " = ?",
                new String[]{template.getId().toString()}
        );
    }

    public void deleteTemplate(TimerTemplate template) {
        mDatabase.delete(TimerTemplateTable.NAME, TimerTemplateTable.Cols.UUID + " = ?",
                new String[]{template.getId().toString()}
        );
    }
}
