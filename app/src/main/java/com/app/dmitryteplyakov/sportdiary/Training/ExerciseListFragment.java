package com.app.dmitryteplyakov.sportdiary.Training;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
 * Created by dmitry21 on 03.08.17.
 */

public class ExerciseListFragment extends Fragment {

    private static final String ARG_DAY_UUID = "com.app.exerciselistfragment.arg_day_uuid";
    private RecyclerView mRecyclerView;
    private ExerciseAdapter mAdapter;

    public static ExerciseListFragment newInstance(UUID dayId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DAY_UUID, dayId);
        ExerciseListFragment fragment = new ExerciseListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_program_exercise, null);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_list_program_exercise_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    private class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Exercise mExercise;
        private TextView mExerciseTitle;

        public ExerciseHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mExerciseTitle = (TextView) itemView.findViewById(R.id.group_name);
        }

        public void bindExercise(Exercise exercise) {
            mExerciseTitle.setText(exercise.getTitle());
            mExercise = exercise;
            Log.d("ELF PARENTCHECK", "EXTITLE: " + mExercise.getTitle() + " ALREADYENDED " + Boolean.toString(mExercise.isAlreadyEnded()) + " DAY ID: " + mExercise.getParentTrainingDayId() + " UUID: " + mExercise.getId());
            //Log.d("ELF", exercise.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = ExerciseActivity.newIntent(getActivity(), mExercise.getReserveId());
            Log.d("ELF", "IS ALREADY WORKING: " + Boolean.toString(mExercise.isAlreadyWorking()));
            if(!mExercise.isAlreadyWorking()) {
                mExercise.setStartDate(new Date());
                mExercise.setAlreadyWorking(true);
                //CompExerciseStorage.get(getActivity()).updateExercise(mExercise);
                Log.d("ELF", "IS ALREADY WORKING: " + Boolean.toString(mExercise.isAlreadyWorking()));
            }
            CompExerciseStorage.get(getActivity()).updateExercise(mExercise);
            Log.d("ELF TEST", Boolean.toString(CompExerciseStorage.get(getActivity()).getExercise(mExercise.getId()).isAlreadyEnded()));
            startActivity(intent);
        }
    }

    private class ExerciseAdapter extends RecyclerView.Adapter<ExerciseHolder> {

        private List<Exercise> mExercises;
        public ExerciseAdapter(List<Exercise> exercises) {
            mExercises = exercises;
        }

        @Override
        public ExerciseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.programs_list_item, parent, false);
            return new ExerciseHolder(view);
        }

        @Override
        public void onBindViewHolder(ExerciseHolder holder, int position) {
            Exercise exercise = mExercises.get(position);
            holder.bindExercise(exercise);
        }

        @Override
        public int getItemCount() {
            return mExercises.size();
        }

        public void setExercises(List<Exercise> exercises) {
            mExercises = exercises;
        }
    }

    private void updateUI() {
        List<Exercise> exerciseList;
        Day day = DayStorage.get(getActivity()).getDay((UUID) getArguments().getSerializable(ARG_DAY_UUID));
        Training training = CompTrainingStorage.get(getActivity()).getTrainingByParentDayId(day.getId());
        Log.d("ELF", "ID DAY: " + day.getId().toString());
        exerciseList = CompExerciseStorage.get(getActivity()).getExercisesByParentTrainingDayId(day.getId());
        Log.d("ELF", "COUNT TRAININGS: " + Integer.toString(CompTrainingStorage.get(getActivity()).getTrainings().size()));
        Log.d("ELF", "ID TRAINING: " + training.getId().toString());
        Log.d("ELF", "TRAINING TITLE: " + training.getTitle());
        Log.d("ELF", "COUNT EXERCISES IN DAY: " + Integer.toString(exerciseList.size()));
        Log.d("ELF", "TOTAL COUNT EXERCISES: " + Integer.toString(CompExerciseStorage.get(getActivity()).getExercises().size()));
        if(mAdapter == null) {
            mAdapter = new ExerciseAdapter(exerciseList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setExercises(exerciseList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

}
