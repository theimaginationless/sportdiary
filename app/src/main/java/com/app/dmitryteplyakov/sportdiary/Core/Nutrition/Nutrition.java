package com.app.dmitryteplyakov.sportdiary.Core.Nutrition;

import java.util.UUID;

/**
 * Created by dmitry21 on 11.08.17.
 */

public class Nutrition {
    private String mProductTitle;
    private UUID mId;
    private UUID mParentDay;
    private UUID mAssociatedDay;
    private int mEnergy;
    private double mWeight;
    private boolean mIsAssociatedWithDay;

    public Nutrition(UUID mId, UUID mParentDay) {
        this.mId = mId;
        this.mParentDay = mParentDay;
        this.mAssociatedDay = UUID.randomUUID();
        this.setAssociatedWithDay(false);
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

    public UUID getAssociatedDay() {
        return mAssociatedDay;
    }

    public void setAssociatedDay(UUID associatedDay) {
        mAssociatedDay = associatedDay;
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

    public boolean isAssociatedWithDay() {
        return mIsAssociatedWithDay;
    }

    public void setAssociatedWithDay(boolean associatedWithDay) {
        mIsAssociatedWithDay = associatedWithDay;
    }
}
