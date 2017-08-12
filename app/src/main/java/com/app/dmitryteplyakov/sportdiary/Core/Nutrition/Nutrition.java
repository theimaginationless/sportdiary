package com.app.dmitryteplyakov.sportdiary.Core.Nutrition;

import java.util.UUID;

/**
 * Created by dmitry21 on 11.08.17.
 */

public class Nutrition {
    private String mProductTitle;
    private UUID mId;
    private UUID mParentDay;
    private int mEnergy;
    private double mWeight;


    public Nutrition(UUID mId, UUID mParentDay) {
        this.mId = mId;
        this.mParentDay = mParentDay;
    }

    public Nutrition() {
        this(UUID.randomUUID(), UUID.randomUUID());
    }

    public UUID getId() {
        return mId;
    }

    public String getProductTitle() {
        return mProductTitle;
    }

    public void setProductTitle(String productTitle) {
        mProductTitle = productTitle;
    }

    public UUID getParentDay() {
        return mParentDay;
    }

    public void setParentDay(UUID parentDay) {
        mParentDay = parentDay;
    }

    public int getEnergy() {
        return mEnergy;
    }

    public void setEnergy(int energy) {
        mEnergy = energy;
    }

    public double getWeight() {
        return mWeight;
    }

    public void setWeight(double weight) {
        mWeight = weight;
    }
}
