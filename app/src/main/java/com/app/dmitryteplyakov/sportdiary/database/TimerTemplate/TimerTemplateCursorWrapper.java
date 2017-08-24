package com.app.dmitryteplyakov.sportdiary.database.TimerTemplate;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.app.dmitryteplyakov.sportdiary.Core.TimerTemplate.TimerTemplate;

import java.util.UUID;

import static com.app.dmitryteplyakov.sportdiary.database.TimerTemplate.TimerTemplateDbSchema.*;

/**
 * Created by dmitry21 on 24.08.17.
 */

public class TimerTemplateCursorWrapper extends CursorWrapper {
    public TimerTemplateCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public TimerTemplate getTemplate() {
        String titleString = getString(getColumnIndex(TimerTemplateTable.Cols.TITLE));
        String uuidString = getString(getColumnIndex(TimerTemplateTable.Cols.UUID));

        TimerTemplate template = new TimerTemplate();
        template.setTitle(titleString);
        template.setId(UUID.fromString(uuidString));

        return template;
    }

}
