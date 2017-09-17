package com.app.dmitryteplyakov.sportdiary.Timer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.app.dmitryteplyakov.sportdiary.Core.Timer.Timer;
import com.app.dmitryteplyakov.sportdiary.Core.Timer.TimerStorage;
import com.app.dmitryteplyakov.sportdiary.Core.TimerTemplate.TimerTemplate;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.UUID;

/**
 * Created by dmitry21 on 19.08.17.
 */

public class TimerFragment extends Fragment {
    private EditText mPreparingTimePicker;
    private EditText mWorkingTimePicker;
    private EditText mRestTimePicker;
    private EditText mIterationsTimePicker;
    private EditText mSetsTimePicker;
    private EditText mTimerTitle;
    private EditText mRestBetweenSetsPicker;
    private static final String ARG_TIMER_UUID = "com.app.timerfragment.arg_timer_uuid";
    Timer mTimer;

    public static Fragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIMER_UUID, id);
        TimerFragment fragment = new TimerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer, parent, false);
        mPreparingTimePicker = (EditText) v.findViewById(R.id.preparing_picker);
        mWorkingTimePicker = (EditText) v.findViewById(R.id.working_picker);
        mRestTimePicker = (EditText) v.findViewById(R.id.rest_picker);
        mIterationsTimePicker = (EditText) v.findViewById(R.id.iterations_picker);
        mSetsTimePicker = (EditText) v.findViewById(R.id.sets_picker);
        mTimerTitle = (EditText) v.findViewById(R.id.timer_title);
        mRestBetweenSetsPicker = (EditText) v.findViewById(R.id.restbetweensets);
        mTimer = TimerStorage.get(getActivity()).getTimer((UUID) getArguments().getSerializable(ARG_TIMER_UUID));
        mPreparingTimePicker.setText(Integer.toString(mTimer.getPreparing()));
        mWorkingTimePicker.setText(Integer.toString(mTimer.getWorkout()));
        mRestTimePicker.setText(Integer.toString(mTimer.getRest()));
        mIterationsTimePicker.setText(Integer.toString(mTimer.getIterations()));
        mSetsTimePicker.setText(Integer.toString(mTimer.getSets()));
        mRestBetweenSetsPicker.setText(Integer.toString(mTimer.getRestBetweenSets()));

        if(mTimer.getTitle() != null)
            mTimerTitle.setText(mTimer.getTitle());

        mTimerTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable c) {
                if(c.toString().equals(""))
                    mTimer.setTitle(getString(R.string.timer_no_title) + " " + Integer.toString(TimerStorage.get(getActivity()).getTimers().size() - TimerStorage.get(getActivity()).getTimers().indexOf(mTimer)));
                else
                    mTimer.setTitle(c.toString());
                TimerStorage.get(getActivity()).updateTimer(mTimer);
            }
        });

        mPreparingTimePicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable c) {
                if(c.toString().equals(""))
                    mTimer.setPreparing(0);
                else
                    mTimer.setPreparing(Integer.parseInt(c.toString()));
                TimerStorage.get(getActivity()).updateTimer(mTimer);
                for(Integer i : mTimer.getTimerValues())
                    Log.d("TF", "CHANGED!" + Integer.toString(i));
            }
        });

        mWorkingTimePicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable c) {
                if(c.toString().equals(""))
                    mTimer.setWorkout(0);
                else
                    mTimer.setWorkout(Integer.parseInt(c.toString()));
                TimerStorage.get(getActivity()).updateTimer(mTimer);
            }
        });

        mRestBetweenSetsPicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable c) {
                if (c.toString().equals(""))
                    mTimer.setRestBetweenSets(0);
                else
                    mTimer.setRestBetweenSets(Integer.parseInt(c.toString()));
                TimerStorage.get(getActivity()).updateTimer(mTimer);
            }
        });

        mRestTimePicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable c) {
                if(c.toString().equals(""))
                    mTimer.setRest(0);
                else
                    mTimer.setRest(Integer.parseInt(c.toString()));
                TimerStorage.get(getActivity()).updateTimer(mTimer);
            }
        });

        mIterationsTimePicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable c) {
                if(c.toString().equals(""))
                    mTimer.setIterations(0);
                else
                    mTimer.setIterations(Integer.parseInt(c.toString()));
                TimerStorage.get(getActivity()).updateTimer(mTimer);
            }
        });

        mSetsTimePicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable c) {
                if(c.toString().equals(""))
                    mTimer.setSets(0);
                else
                    mTimer.setSets(Integer.parseInt(c.toString()));
                TimerStorage.get(getActivity()).updateTimer(mTimer);
                Log.d("TF", "After change " + c.toString());
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mTimer.getTitle() == null) {
            mTimer.setTitle(getString(R.string.timer_no_title) + " " + Integer.toString(TimerStorage.get(getActivity()).getTimers().size() - TimerStorage.get(getActivity()).getTimers().indexOf(mTimer)));
            TimerStorage.get(getActivity()).updateTimer(mTimer);
            Log.d("TF", "UPDATED!");
        }
    }
}
