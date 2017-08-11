package com.app.dmitryteplyakov.sportdiary.Core.Exercise;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Dmitry on 22.07.2017.
 * One exercise
 */

public class Exercise implements Comparable<Exercise> {
    private String mTitle;
    private String mGroupTitle;
    private Date mStartDate; // Started time of exercise
    private Date mEndDate; // Ended time of exercise
    private int mReplays;
    private int mEnergy;
    private int mApproach;
    private boolean needTimer;
    private double weight;
    private UUID mId;
    private UUID mParentUUID;
    private UUID mParentTrainingDayId;
    private UUID mReserveId;
    private boolean mNeedPullCounter;
    private boolean mAlreadyWorking;
    private boolean mAlreadyEnded;
    private UUID mParentDayId;

    public Exercise(UUID id, UUID parentId) {
        mId = id;
        mParentUUID = parentId;
        mStartDate = new Date();
        mEndDate = new Date();
        mParentTrainingDayId = UUID.randomUUID();
        mReserveId = UUID.randomUUID();
        mParentDayId = UUID.randomUUID();
    }

    public Exercise() {
        this(UUID.randomUUID(), UUID.randomUUID());
    }

    public UUID getParentDayId() {
        return mParentDayId;
    }

    public void setParentDayId(UUID parentDayId) {
        mParentDayId = parentDayId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getReplays() {
        return mReplays;
    }

    public void setReplays(int replays) {
        mReplays = replays;
    }

    public int getApproach() {
        return mApproach;
    }

    public void setApproach(int approach) {
        mApproach = approach;
    }

    public boolean isNeedTimer() {
        return needTimer;
    }

    public void setNeedTimer(boolean needTimer) {
        this.needTimer = needTimer;
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

    public UUID getId() {
        return mId;
    }

    public UUID getParentUUID() {
        return mParentUUID;
    }

    public void setParentUUID(UUID parentUUID) {
        this.mParentUUID = parentUUID;
    }

    public String getGroupTitle() {
        return mGroupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        mGroupTitle = groupTitle;
    }

    public int getEnergy() {
        return mEnergy;
    }

    public void setEnergy(int energy) {
        mEnergy = energy;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String toString() {
        return mTitle;
    }

    public UUID getParentTrainingDayId() {
        return mParentTrainingDayId;
    }

    public void setParentTrainingDayId(UUID parentTrainingDayId) {
        mParentTrainingDayId = parentTrainingDayId;
    }

    public boolean isNeedPullCounter() {
        return mNeedPullCounter;
    }

    public void setNeedPullCounter(boolean needPullCounter) {
        mNeedPullCounter = needPullCounter;
    }

    public boolean isAlreadyWorking() {
        return mAlreadyWorking;
    }

    public void setAlreadyWorking(boolean alreadyWorking) {
        mAlreadyWorking = alreadyWorking;
    }

    public boolean isAlreadyEnded() {
        return mAlreadyEnded;
    }

    public void setAlreadyEnded(boolean alreadyEnded) {
        mAlreadyEnded = alreadyEnded;
    }

    public UUID getReserveId() {
        return mReserveId;
    }

    public void setReserveId(UUID reserveId) {
        mReserveId = reserveId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    @Override
    public boolean equals(Object o) {
        if(this.getId().equals(((Exercise) o).getId()))
            return true;
        return false;
    }

    @Override
    public int compareTo(Exercise exercise) { // 1 - this is upper(0), -1 - this is down(size() - 1)
        if(this.getStartDate().after(exercise.getStartDate()))
            return 1;
        return -1;
    }
}
