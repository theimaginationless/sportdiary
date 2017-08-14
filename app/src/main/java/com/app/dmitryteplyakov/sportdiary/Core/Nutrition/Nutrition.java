package com.app.dmitryteplyakov.sportdiary.Core.Nutrition;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.UUID;

/**
 * Created by dmitry21 on 11.08.17.
 */

public class Nutrition {
    private String mProductTitle;
    private UUID mId;
    private UUID mParentDay;
    private int mEnergy;
    private float mWeight;
    private int mResultEnergy;


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

    public float getWeight() {
        return mWeight;
    }

    public void setWeight(float weight) {
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
        dfs.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#0.00");
        df.setDecimalFormatSymbols(dfs);
        String formattedFloat = df.format(weight);
        mWeight = Float.parseFloat(formattedFloat);
    }

    public int getResultEnergy() {
        return mResultEnergy;
    }

    public void setResultEnergy(int resultEnergy) {
        mResultEnergy = resultEnergy;
    }

    @Override
    public boolean equals(Object o) {
        if(this.getId().equals(((Nutrition) o).getId()))
            return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if(this.getId().equals(((Nutrition) o).getId()))
            return true;
        return false;
    }
}
