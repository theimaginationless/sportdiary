package com.app.dmitryteplyakov.sportdiary.Core.TimerTemplate;

import java.util.UUID;

/**
 * Created by dmitry21 on 24.08.17.
 */

public class TimerTemplate {
    private String mTitle;
    private UUID mId;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String toString() {
        return mTitle;
    }

    @Override
    public boolean equals(Object o) {
        if(this.getId().equals(((TimerTemplate) o).getId()))
            return true;
        return false;
    }
}
