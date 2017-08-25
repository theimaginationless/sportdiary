package com.app.dmitryteplyakov.sportdiary.Core.Timer;

import java.util.UUID;

/**
 * Created by dmitry21 on 20.08.17.
 */

public class Timer {
    private String mTitle;
    private UUID mId;
    private UUID mParent;
    private int iterations;
    private int preparing;
    private int workout;
    private int rest;
    private int sets;
    private int restBetweenSets;
    private int calmDown;

    public Timer() {
        mId = UUID.randomUUID();
        mParent = UUID.randomUUID();
    }

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

    public UUID getParent() {
        return mParent;
    }

    public void setParent(UUID parent) {
        this.mParent = parent;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getPreparing() {
        return preparing;
    }

    public void setPreparing(int preparing) {
        this.preparing = preparing;
    }

    public int getWorkout() {
        return workout;
    }

    public void setWorkout(int workout) {
        this.workout = workout;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getRestBetweenSets() {
        return restBetweenSets;
    }

    public void setRestBetweenSets(int restBetweenSets) {
        this.restBetweenSets = restBetweenSets;
    }

    public int getCalmDown() {
        return calmDown;
    }

    public void setCalmDown(int calmDown) {
        this.calmDown = calmDown;
    }

    @Override
    public boolean equals(Object o) {
        if(this.getId().equals(((Timer) o).getId()))
            return true;
        return false;
    }
}
