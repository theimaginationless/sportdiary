package com.app.dmitryteplyakov.sportdiary.Core.Timer;

import java.util.ArrayList;
import java.util.List;
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
    /*
    4 - iterations
    0 - preparing
    1 - workout
    2 - rest
    5 - sets
    3 restbetweensets
    6 - calmDown
     */
    private List<Integer> timerValues;

    public Timer() {
        timerValues = new ArrayList<>();
        mId = UUID.randomUUID();
        mParent = UUID.randomUUID();
        for(int i = 0; i < 6; i++)
            timerValues.add(i, 0);
    }

    public List<Integer> getTimerValues() {
        return timerValues;
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
        return timerValues.get(4);
    }

    public void setIterations(int iterations) {
        timerValues.set(4, iterations);
    }

    public int getPreparing() {
        return timerValues.get(0);
    }

    public void setPreparing(int preparing) {
        timerValues.set(0, preparing);
    }

    public int getWorkout() {
        return timerValues.get(1);
    }

    public void setWorkout(int workout) {
        timerValues.set(1, workout);
    }

    public int getRest() {
        return timerValues.get(2);
    }

    public void setRest(int rest) {
        timerValues.set(2, rest);
    }

    public int getSets() {
        return timerValues.get(5);
    }

    public void setSets(int sets) {
        timerValues.set(5, sets);
    }

    public int getRestBetweenSets() {
        return timerValues.get(3);
    }

    public void setRestBetweenSets(int restBetweenSets) {
        timerValues.set(3, restBetweenSets);
    }

    public int getCalmDown() {
        //return timerValues.get(6);
        return 0;
    }

    public void setCalmDown(int calmDown) {
        //timerValues.set(6, calmDown);
    }

    @Override
    public boolean equals(Object o) {
        if(this.getId().equals(((Timer) o).getId()))
            return true;
        return false;
    }
}
