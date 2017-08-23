package com.app.dmitryteplyakov.sportdiary.Programs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.app.dmitryteplyakov.sportdiary.Core.Exercise.CompExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.Exercise;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.ExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Training.TrainingStorage;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.UUID;

/**
 * Created by Dmitry on 23.07.2017.
 */

public class ProgramFragment extends Fragment {

    private static final String ARGS_EXERCISE_UUID = "com.app.args_exercise_uuid";
    public static final String EXTRA_EXERCISE_BACK_UUID = "com.app.extra_exercise_back_uuid";
    private EditText mExerciseTitleEditText;
    private EditText mExerciseReplaysEditText;
    private EditText mExerciseApproachEditText;
    private Switch mExerciseIntervalTimerSwitch;
    private Switch mExercisePullCounterSwitch;
    private EditText mExerciseWeightEditText;
    private EditText mExerciseEnergyEditText;
    private Exercise mExercise;
    private LinearLayout mWorkaroundFocusLinearLayout;

    public static ProgramFragment newInstance(UUID parentId) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_EXERCISE_UUID, parentId);
        ProgramFragment fragment = new ProgramFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mExercise = ExerciseStorage.get(getActivity()).getExercise((UUID) getArguments().getSerializable(ARGS_EXERCISE_UUID));
        if(mExercise.getTitle() == null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.exercise_no_title) + " " + Integer.toString(ExerciseStorage.get(getActivity()).getExercisesByParentId(mExercise.getParentUUID()).indexOf(mExercise) + 1));
        else ((AppCompatActivity) getActivity()).setTitle(mExercise.getTitle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_program, container, false);
        mWorkaroundFocusLinearLayout = (LinearLayout) v.findViewById(R.id.workaround_focus);
        if(mExercise.getTitle() == null)
            mWorkaroundFocusLinearLayout.setFocusableInTouchMode(false);
        mExerciseWeightEditText = (EditText) v.findViewById(R.id.fragment_program_exercise_weight);
        mExerciseTitleEditText = (EditText) v.findViewById(R.id.fragment_program_exercise_title);
        mExerciseReplaysEditText = (EditText) v.findViewById(R.id.fragment_program_exercise_replays_count);
        mExerciseApproachEditText = (EditText) v.findViewById(R.id.fragment_program_exercise_approach_count);
        mExercisePullCounterSwitch = (Switch) v.findViewById(R.id.fragment_program_pull_counter_switch);
        mExerciseEnergyEditText = (EditText) v.findViewById(R.id.fragment_program_exercise_energy);
        if(mExercise.getEnergy() != 0)
            mExerciseEnergyEditText.setText(Integer.toString(mExercise.getEnergy()));
        mExerciseEnergyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if(c.toString().equals(""))
                    mExercise.setEnergy(0);
                else
                    mExercise.setEnergy(Integer.parseInt(c.toString()));
            }

            @Override
            public void afterTextChanged(Editable c) {
                CompExerciseStorage.get(getActivity()).updateExercise(mExercise);
            }
        });

        mExercisePullCounterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mExercise.setNeedPullCounter(isChecked);
            }
        });
        mExerciseIntervalTimerSwitch = (Switch) v.findViewById(R.id.fragment_program_interval_timer_switch);

        mExerciseIntervalTimerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mExercise.setNeedTimer(isChecked);
            }
        });
        mExercisePullCounterSwitch.setChecked(mExercise.isNeedPullCounter());
        mExerciseIntervalTimerSwitch.setChecked(mExercise.isNeedTimer());
        mExerciseTitleEditText.setText(mExercise.getTitle());
        mExerciseReplaysEditText.setText(Integer.toString(mExercise.getReplays()));
        mExerciseApproachEditText.setText(Integer.toString(mExercise.getApproach()));
        if(mExercise.getWeight() != 0)
            mExerciseWeightEditText.setText(Float.toString(mExercise.getWeight()));

        mExerciseTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                if(mExercise.getTitle() == null)
                    mExercise.setTitle(getString(R.string.exercise_no_title) + " " + Integer.toString(ExerciseStorage.get(getActivity()).getExercisesByParentId(mExercise.getParentUUID()).indexOf(mExercise) + 1));
            }
            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if(count == 0)
                    mExercise.setTitle(getString(R.string.exercise_no_title) + " " + Integer.toString(ExerciseStorage.get(getActivity()).getExercisesByParentId(mExercise.getParentUUID()).indexOf(mExercise) + 1));
                else
                    mExercise.setTitle(c.toString());
            }
            @Override
            public void afterTextChanged(Editable c) {
                CompExerciseStorage.get(getActivity()).updateExercise(mExercise);
            }
        });

        mExerciseWeightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if(c.toString().equals(""))
                    mExercise.setWeight(0.0f);
                else
                    mExercise.setWeight(Float.parseFloat(c.toString()));
            }

            @Override
            public void afterTextChanged(Editable c) {
                CompExerciseStorage.get(getActivity()).updateExercise(mExercise);
            }
        });

        mExerciseReplaysEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
              //
            }
            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if(c.toString().equals(""))
                    mExercise.setReplays(0);
                else
                    mExercise.setReplays(Integer.parseInt(c.toString()));
            }
            @Override
            public void afterTextChanged(Editable c) {
                CompExerciseStorage.get(getActivity()).updateExercise(mExercise);
            }
        });

        mExerciseApproachEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                //
            }
            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if(c.toString().equals(""))
                    mExercise.setApproach(0);
                else
                    mExercise.setApproach(Integer.parseInt(c.toString()));
            }
            @Override
            public void afterTextChanged(Editable c) {
                CompExerciseStorage.get(getActivity()).updateExercise(mExercise);
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExercise.getTitle() == null)
            mExercise.setTitle(getString(R.string.exercise_no_title) + " " + Integer.toString(ExerciseStorage.get(getActivity()).getExercisesByParentId(mExercise.getParentUUID()).indexOf(mExercise) + 1));
        ExerciseStorage.get(getActivity()).updateExercise(mExercise);
    }
}
