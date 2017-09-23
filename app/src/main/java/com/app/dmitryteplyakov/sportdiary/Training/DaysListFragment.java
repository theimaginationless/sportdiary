package com.app.dmitryteplyakov.sportdiary.Training;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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
import com.app.dmitryteplyakov.sportdiary.Core.Training.TrainingStorage;
import com.app.dmitryteplyakov.sportdiary.Dialogs.DeleteFragment;
import com.app.dmitryteplyakov.sportdiary.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class DaysListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ExerciseAdapter mAdapter;
    private FloatingActionButton mFAB;
    private TextView mEmptyTextView;
    public static final int REQUEST_DAYS_LIST_FRAGMENT = 7;
    public static final int REQUEST_DELETE_DAY = 9;
    public static final String DIALOG_DAY_DELETE = "com.app.dayslistfragment.dialog_day_delete";
    private LinearLayoutManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_days_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.training_list_fragment_recycler_view);
        manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mFAB = (FloatingActionButton) getActivity().findViewById(R.id.activity_common_fab_add);
        mEmptyTextView = (TextView) v.findViewById(R.id.fragment_days_list_empty_text_view);
        mEmptyTextView.setText(getString(R.string.fragment_days_list_empty_text));
        mRecyclerView.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), manager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0)
                    mFAB.hide();
                else if(dy < 0)
                    mFAB.show();
            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });

        return v;
    }
    private class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView dateTextView;
        private TextView countExercises;
        private TextView durationExercises;
        private TextView exerciseGroup;
        private Day mDay;
        private CharSequence options[];

        public ExerciseHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            options = new CharSequence[] {getString(R.string.menu_delete_item)};
            dateTextView = (TextView) itemView.findViewById(R.id.days_list_item_date_text_view);
            countExercises = (TextView) itemView.findViewById(R.id.days_list_item_count_exercises_text_view);
            durationExercises = (TextView) itemView.findViewById(R.id.days_list_item_duration_exercises_text_view);
            exerciseGroup = (TextView) itemView.findViewById(R.id.days_list_item_group_exercises_text_view);
        }

        @Override
        public void onClick(View v) {
            Intent intent = ExerciseListActivity.newIntent(getActivity(), mDay.getId());
            Log.d("DLF", "STARTLIST: DATE: " + mDay.getDate().toString());
            startActivityForResult(intent, REQUEST_DAYS_LIST_FRAGMENT);
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder optionsDialog = new AlertDialog.Builder(getActivity());
            optionsDialog.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which) {
                        case 0:
                            FragmentManager manager = getFragmentManager();
                            DeleteFragment optionDialog = DeleteFragment.newInstance(mDay.getId());
                            optionDialog.setTargetFragment(DaysListFragment.this, REQUEST_DELETE_DAY);
                            optionDialog.show(manager, DIALOG_DAY_DELETE);
                    }
                }
            });
            optionsDialog.show();
            return true;
        }

        public void setDay(Day day) {
            mDay = day;
        }
    }

    private class ExerciseAdapter extends RecyclerView.Adapter<ExerciseHolder> {
        private List<Day> mDays;

        public ExerciseAdapter(List<Day> days) {
            mDays = days;
        }

        @Override
        public ExerciseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.days_list_item, parent, false);
            return new ExerciseHolder(view);
        }

        @Override
        public void onBindViewHolder(ExerciseHolder holder, int position) {
            Day day = mDays.get(position);
            SimpleDateFormat format = new SimpleDateFormat("d MMMM yyyy");
            UUID trainingId = day.getTrainingId();
            List<Exercise> exerciseList = CompExerciseStorage.get(getActivity()).getExercisesByParentTrainingDayId(day.getId());
            int count = 0;
            for(Exercise exercise : exerciseList) {
                if (exercise.isAlreadyEnded())
                    count++;
            }
            holder.countExercises.setText(Integer.toString(count) + "/" + Integer.toString(exerciseList.size()));
            holder.dateTextView.setText(format.format(day.getDate()));
            holder.exerciseGroup.setText(CompTrainingStorage.get(getActivity()).getTraining(trainingId).getTitle());
            List<Exercise> compExerciseList = CompExerciseStorage.get(getActivity()).getExercisesByParentTrainingDayId(day.getId());
            Log.d("DLF", Integer.toString(compExerciseList.size()));
            if(exerciseList.size() != 0) {
                Collections.sort(compExerciseList);
                day.setStartDate(compExerciseList.get(0).getStartDate());
                day.setEndDate(compExerciseList.get(compExerciseList.size() - 1).getEndDate());
                DayStorage.get(getActivity()).updateDay(day);
                long duration = (day.getEndDate().getTime() - day.getStartDate().getTime()) / 60000;
                if(duration < 0)
                    holder.durationExercises.setText(getString(R.string.time_na));
                else {
                    //holder.durationExercises.setText(getResources().getQuantityString(R.plurals.minutes_plural, (int) duration, (int) duration));
                    if(duration / 60 == 0)
                        holder.durationExercises.setText(getResources().getQuantityString(R.plurals.minutes_plural, (int) duration, (int) duration));
                    else {
                        int hour = (int) duration / 60;
                        int minute = (int) duration % 60;
                        holder.durationExercises.setText(getResources().getQuantityString(R.plurals.hour_plural, hour, hour) + " " + getResources().getQuantityString(R.plurals.minutes_plural, minute, minute));
                    }
                }
            } else
                holder.durationExercises.setText(getString(R.string.time_na));
            holder.setDay(day);
        }

        @Override
        public int getItemCount() {
            return mDays.size();
        }

        public void setDays(List<Day> days) {
            mDays = days;
        }
    }

    private void updateUI() {
        List<Day> day = DayStorage.get(getActivity()).getDays();
        if(mAdapter == null) {
            mAdapter = new ExerciseAdapter(day);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setDays(day);
            mAdapter.notifyDataSetChanged();
        }
        if(day.size() != 0){
            mEmptyTextView.setVisibility(View.GONE);
        } else {
            mEmptyTextView.setVisibility(View.VISIBLE);
            mFAB.show();
        }
    }

    private void updateUI(boolean isAdd, int num) {
        List<Day> day = DayStorage.get(getActivity()).getDays();
        mAdapter.setDays(day);
        if(isAdd)
            mAdapter.notifyItemInserted(num);
        else
            mAdapter.notifyItemRemoved(num);
        if(day.size() != 0){
            mEmptyTextView.setVisibility(View.GONE);
        } else {
            mEmptyTextView.setVisibility(View.VISIBLE);
            mFAB.show();
        }
        // Todo: Strange behavior
        if(!mRecyclerView.canScrollVertically(1)) { // 1 - down direction
            mFAB.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_DELETE_DAY) {
            if(resultCode == Activity.RESULT_OK) {
                UUID dayId = (UUID) data.getSerializableExtra(DeleteFragment.EXTRA_RETURN_DELETE_UUID);
                Day day = DayStorage.get(getActivity()).getDay(dayId);
                DayStorage dayStorage = DayStorage.get(getActivity());
                CompTrainingStorage compTrainingStorage = CompTrainingStorage.get(getActivity());
                compTrainingStorage.deleteTrainingByDayId(day.getId());
                CompExerciseStorage compExerciseStorage = CompExerciseStorage.get(getActivity());
                compExerciseStorage.deleteExercisesByParentTrainingDayId(day.getId());
                Log.d("DLF", Integer.toString(compExerciseStorage.getExercises().size()));
                final int num = DayStorage.get(getActivity()).getDays().indexOf(day);
                dayStorage.deleteDay(day);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(false, num);
                    }
                });

                //Snackbar mSnackBar = Snackbar.make(getActivity().findViewById(R.id.snackbar_place), getString(R.string.snackbar_day_deleted) , Snackbar.LENGTH_LONG);
                //mSnackBar.show();
            }
        }
    }

}
