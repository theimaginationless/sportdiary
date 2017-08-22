package com.app.dmitryteplyakov.sportdiary.Core.Weight;

import java.util.Date;
import java.util.UUID;

/**
 * Created by dmitry21 on 22.08.17.
 */

public class Weight implements Comparable<Weight> {
    private Date mDate;
    private float mValue;
    private UUID mId;

    public Weight(float mValue) {
        this.setValue(mValue);
        mId = UUID.randomUUID();
        this.setDate(new Date());
    }

    public Weight() {
        this(0);
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public float getValue() {
        return mValue;
    }

    public void setValue(float value) {
        mValue = (((float) ((int) Math.round(value * 100))) / 100);
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    @Override
    public int compareTo(Weight weight) {
        if(this.getDate().after(weight.getDate()))
            return -1;
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if(this.getId().equals(((Weight) o).getId()))
            return true;
        return false;
    }
}
