package com.app.dmitryteplyakov.sportdiary.Training;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.dmitryteplyakov.sportdiary.Core.Exercise.CompExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.Exercise;
import com.app.dmitryteplyakov.sportdiary.Dialogs.TimePickerFragment;
import com.app.dmitryteplyakov.sportdiary.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class ExerciseFragment extends Fragment {

    private Exercise mExercise;
    private static final String ARG_TRAINING_UUID = "com.app.exercisefragment.arg_training_uuid";
    public static final int REQUEST_START_TIME = 12;
    public static final int REQUEST_END_TIME = 13;
    private static final String DIALOG_TIME_PICKER = "com.app.exercisefragment.dialog_date_picker";
    private EditText mCountReplays;
    private EditText mCountApproach;
    private Button mStartDateButton;
    private Button mEndDateButton;
    private Button mEndExerciseButton;
    private EditText mReplaysEditText;
    private EditText mApproachEditText;
    private EditText mWeightEditText;
    private EditText mEnergyEditText;
    private SimpleDateFormat format;

    public static ExerciseFragment newInstance(UUID trainingId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRAINING_UUID, trainingId);
        ExerciseFragment fragment = new ExerciseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_using_exercise, container, false);
        mExercise = CompExerciseStorage.get(getActivity()).getExerciseByReserveId((UUID) getArguments().getSerializable(ARG_TRAINING_UUID));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mExercise.getTitle());
        mEnergyEditText = (EditText) v.findViewById(R.id.fragment_using_exercise_energy_edit_text);
        if(mExercise.getEnergy() != 0)
            mEnergyEditText.setText(Integer.toString(mExercise.getEnergy()));
        mEnergyEditText.addTextChangedListener(new TextWatcher() {
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

        mReplaysEditText = (EditText) v.findViewById(R.id.fragment_using_exercise_count_replays_edit_text);
        mReplaysEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if(count != 0)
                    mExercise.setReplays(Integer.parseInt(c.toString()));
            }

            @Override
            public void afterTextChanged(Editable c) {
                CompExerciseStorage.get(getActivity()).updateExercise(mExercise);
            }
        });

        mApproachEditText = (EditText) v.findViewById(R.id.fragment_using_exercise_count_approach_edit_text);
        mApproachEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if(count != 0)
                    mExercise.setApproach(Integer.parseInt(c.toString()));
            }

            @Override
            public void afterTextChanged(Editable c) {
                CompExerciseStorage.get(getActivity()).updateExercise(mExercise);
            }
        });

        mWeightEditText = (EditText) v.findViewById(R.id.fragment_using_exercise_weight_edit_text);
        if(mExercise.getWeight() != 0)
            mWeightEditText.setText(Float.toString(mExercise.getWeight()));
        mWeightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if(count != 0)
                    mExercise.setWeight(Float.parseFloat(c.toString()));
                Log.d("EF", Float.toString(mExercise.getWeight()));
            }

            @Override
            public void afterTextChanged(Editable c) {
                CompExerciseStorage.get(getActivity()).updateExercise(mExercise);
            }
        });

        mCountReplays = (EditText) v.findViewById(R.id.fragment_using_exercise_count_replays_edit_text);
        if(mExercise.getReplays() != 0)
            mCountReplays.setText(Integer.toString(mExercise.getReplays()));
        mCountApproach = (EditText) v.findViewById(R.id.fragment_using_exercise_count_approach_edit_text);
        if(mExercise.getApproach() != 0)
            mCountApproach.setText(Integer.toString(mExercise.getApproach()));
        format = new SimpleDateFormat("HH:mm");
        mEndDateButton = (Button) v.findViewById(R.id.fragment_using_exercise_end_date_training_button);
        mEndDateButton.setText(format.format(mExercise.getEndDate()));
        mEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mExercise.isAlreadyEnded()) {
                    Toast.makeText(getActivity(), getString(R.string.check_date_with_no_ended_exercise), Toast.LENGTH_SHORT).show();
                } else {
                    FragmentManager manager = getFragmentManager();
                    TimePickerFragment dialog = TimePickerFragment.newInstance(mExercise.getReserveId());
                    dialog.setTargetFragment(ExerciseFragment.this, REQUEST_END_TIME);
                    dialog.show(manager, DIALOG_TIME_PICKER);
                }
            }
        });

        mStartDateButton = (Button) v.findViewById(R.id.fragment_using_exercise_start_date_training_button);
        mStartDateButton.setText(format.format(mExercise.getStartDate()));
        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mExercise.getReserveId());
                dialog.setTargetFragment(ExerciseFragment.this, REQUEST_START_TIME);
                dialog.show(manager, DIALOG_TIME_PICKER);
            }
        });
        if(!mExercise.isAlreadyEnded()) {
            mEndDateButton.setText(getString(R.string.fragment_using_exercise_end_training_waiting));
        }
        mEndExerciseButton = (Button) v.findViewById(R.id.fragment_using_exercise_end_training_button);
        if(!mExercise.isAlreadyEnded()) {
            mEndExerciseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExercise.setEndDate(new Date());
                    mExercise.setAlreadyEnded(true);
                    CompExerciseStorage.get(getActivity()).updateExercise(mExercise);
                    mEndExerciseButton.setEnabled(!mExercise.isAlreadyEnded());
                    mEndDateButton.setText(format.format(mExercise.getEndDate()));}
            });
        } else
            mEndExerciseButton.setEnabled(!mExercise.isAlreadyEnded());
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        CompExerciseStorage.get(getActivity()).updateExercise(mExercise);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.RETURN_TIME);
            if (requestCode == REQUEST_START_TIME) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mExercise.getStartDate());
                Calendar newTime = Calendar.getInstance();
                newTime.setTime(date);
                calendar.set(Calendar.DAY_OF_MONTH, newTime.get(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, newTime.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, newTime.get(Calendar.MINUTE));
                calendar.set(Calendar.SECOND, newTime.get(Calendar.SECOND));
                calendar.set(Calendar.MILLISECOND, newTime.get(Calendar.MILLISECOND));
                mExercise.setStartDate(calendar.getTime());
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                mStartDateButton.setText(format.format(mExercise.getStartDate()));
            } else if (requestCode == REQUEST_END_TIME) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mExercise.getStartDate());
                Calendar newTime = Calendar.getInstance();
                newTime.setTime(date);
                calendar.set(Calendar.DAY_OF_MONTH, newTime.get(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, newTime.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, newTime.get(Calendar.MINUTE));
                calendar.set(Calendar.SECOND, newTime.get(Calendar.SECOND));
                calendar.set(Calendar.MILLISECOND, newTime.get(Calendar.MILLISECOND));
                mExercise.setEndDate(calendar.getTime());
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                mEndDateButton.setText(format.format(mExercise.getEndDate()));
            }
            CompExerciseStorage.get(getActivity()).updateExercise(mExercise);
        }
    }
}
