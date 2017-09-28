package com.app.dmitryteplyakov.sportdiary.Training;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.app.dmitryteplyakov.sportdiary.Core.Day.Day;
import com.app.dmitryteplyakov.sportdiary.Core.Day.DayStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.CompExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.Exercise;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.ExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Training.CompTrainingStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Training.Training;
import com.app.dmitryteplyakov.sportdiary.Core.Training.TrainingStorage;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dmitry on 01.08.2017.
 */

public class NewDayFragment extends Fragment {

    public static final String ARG_NEW_DAY_UUID = "com.app.newdayfragment.arg_new_day_uuid";
    public static final String EXTRA_RETURN_DAY_UUID = "com.app.newdayfragment.extra_return_training_uuid";
    private Spinner mTrainingsSpinner;
    private Day mDay;
    private int mCounterOfSpinner;
    private int mPosition;
    private long mId;
    private Button mStartButton;
    public static int REQUEST_NEW_DAY_FRAGMENT = 24;

    public static NewDayFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NEW_DAY_UUID, id);
        NewDayFragment fragment = new NewDayFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_day, null);
        mPosition = -1;
        mTrainingsSpinner = (Spinner) v.findViewById(R.id.trainings_spinner);
        mStartButton = (Button) v.findViewById(R.id.fragment_new_day_start_training_button);
        final List<Training> listTrainings = TrainingStorage.get(getActivity()).getTrainings();
        ArrayAdapter<Training> adapter = new ArrayAdapter<Training>(getActivity(), android.R.layout.simple_spinner_item, listTrainings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTrainingsSpinner.setAdapter(adapter);
        mTrainingsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                mId = id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPosition = 0;
            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayStorage dayStorage = DayStorage.get(getActivity());
                Intent intent = ExerciseListActivity.newIntent(getActivity(), DayStorage.get(getActivity()).getDay((UUID) getArguments().getSerializable(ARG_NEW_DAY_UUID)).getId());
                startActivityForResult(intent, REQUEST_NEW_DAY_FRAGMENT);
                getActivity().finish();
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mPosition != -1) {
            List<Training> listTrainings = TrainingStorage.get(getActivity()).getTrainings();
            DayStorage dayStorage = DayStorage.get(getActivity());
            Day day = dayStorage.getDay((UUID) getArguments().getSerializable(ARG_NEW_DAY_UUID));
            Log.d("NDF", listTrainings.get(mPosition).getTitle() + " " + listTrainings.get(mPosition).getId().toString());
            Training training = listTrainings.get(mPosition);
            training.setParentDayId(day.getId());
            CompTrainingStorage.get(getActivity()).addTraining(training);
            Log.d("NDF", "NULL TRAINING: " + Boolean.toString(CompTrainingStorage.get(getActivity()).getTrainings() == null));
            Log.d("NDF", "TRAINING DAY ID: " + training.getParentDayId().toString());
            day.setTrainingId(training.getId());
            dayStorage.updateDay(day);
            List<Exercise> exerciseList = ExerciseStorage.get(getActivity()).getExercisesByParentId(training.getId());

            //INFO: IMPORTANT! USE ID FOR CHECK TOTAL INFO ABOUT EXERCISE!
            CompExerciseStorage compExStorage = CompExerciseStorage.get(getActivity());
            for (Exercise exercise : exerciseList) {
                exercise.setStartDate(new Date());
                exercise.setEndDate(new Date());
                exercise.setParentDayId(day.getId());
                exercise.setReserveId(UUID.randomUUID());
                exercise.setParentTrainingDayId(day.getId());
                compExStorage.addExercise(exercise);
            }
            Log.d("NDF", Integer.toString(compExStorage.getExercises().size()));
        }
    }
}
