package com.app.dmitryteplyakov.sportdiary.Core.Training;

import java.util.UUID;

/**
 * Created by Dmitry on 24.07.2017.
 */

public class Training {
    private UUID mId;
    private String mTitle;
    private UUID mParentId;
    private UUID mParentDayId;

    public Training() {
        mId = UUID.randomUUID();
        mParentId = UUID.randomUUID();
        mParentDayId = UUID.randomUUID();
    }

    public Training(UUID id, UUID parentId) {
        mId = id;
        mParentId = parentId;
        mParentDayId = UUID.randomUUID();
    }

    public Training(UUID parentId) {
        this(UUID.randomUUID(), parentId);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getParentId() {
        return mParentId;
    }

    public void setParentId(UUID parentId) {
        mParentId = parentId;
    }

    public UUID getId() {
        return mId;
    }

    public UUID getParentDayId() {
        return mParentDayId;
    }

    public void setParentDayId(UUID parentDayId) {
        mParentDayId = parentDayId;
    }

    public String toString() {
        return mTitle;
    }

    @Override
    public boolean equals(Object o) {
        if(this.getId().equals(((Training) o).getId()))
            return true;
        return false;
    }
}
