package com.app.dmitryteplyakov.sportdiary.Training;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
    private LinearLayoutManager mLinearLayoutManager;
    public static final int REQUEST_BACK = 15;

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
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(CompTrainingStorage.get(getActivity()).getTrainingByParentDayId((UUID) getArguments().getSerializable(ARG_DAY_UUID)).getTitle());
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
        }

        @Override
        public void onClick(View v) {
            Intent intent = ExerciseActivity.newIntent(getActivity(), mExercise.getReserveId());
            if(!mExercise.isAlreadyWorking()) {
                mExercise.setStartDate(new Date());
                mExercise.setAlreadyWorking(true);
            }
            CompExerciseStorage.get(getActivity()).updateExercise(mExercise);
            startActivityForResult(intent, REQUEST_BACK);
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
        exerciseList = CompExerciseStorage.get(getActivity()).getExercisesByParentTrainingDayId(day.getId());
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
