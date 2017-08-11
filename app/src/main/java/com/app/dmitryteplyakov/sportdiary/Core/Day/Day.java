package com.app.dmitryteplyakov.sportdiary.Core.Day;

import android.util.Log;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Dmitry on 23.07.2017.
 */

public class Day implements Comparable<Day> {
    private String mTitle;
    private UUID mId;
    private UUID trainingId;
    //private UUID mParentDay; // Connected with ParentUUID
    private Date mDate; // Connected with start time
    private Date mStartDate;
    private Date mEndDate;

    public Day(UUID id) {
        //mParentDay = pId;
        mId = id;
        mDate = new Date();
    }
    public Day() {
        this(UUID.randomUUID());
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public UUID getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(UUID trainingId) {
        this.trainingId = trainingId;
    }

    @Override
    public boolean equals(Object o) {
        if(this.getId().equals(((Day) o).getId()))
            return true;
        return false;
    }

    @Override
    public int compareTo(Day day) {
        if(this.getDate().after(day.getDate()))
            return -1;
        return 1;
    }

}
