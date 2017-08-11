package com.app.dmitryteplyakov.sportdiary.Training;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Day.Day;
import com.app.dmitryteplyakov.sportdiary.Core.Day.DayStorage;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class TrainingListFragment extends Fragment {
    private DayAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyTextView;
    private static final String ARG_NEW_DAY_UUID = "com.app.traininglistfragment.arg_new_day_uuid";

    public static TrainingListFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NEW_DAY_UUID, id);
        TrainingListFragment fragment = new TrainingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_training_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_training_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmptyTextView = (TextView) v.findViewById(R.id.fragment_days_list_empty_text_view);

        return v;
    }

    private class DayHolder extends RecyclerView.ViewHolder {
        private Day mDay;
        public DayHolder(View itemView) {
            super(itemView);
        }
        public void bindDay(Day day) {
            mDay = day;
        }
    }

    private class DayAdapter extends RecyclerView.Adapter<DayHolder> {
        private List<Day> mDays;
        public DayAdapter(List<Day> days) {
            mDays = days;
        }
        @Override
        public DayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.days_list_item, null);
            return new DayHolder(view);
        }
        @Override
        public void onBindViewHolder(DayHolder holder, int position) {
            Day day = mDays.get(position);
            holder.bindDay(day);
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
        List<Day> daysList = DayStorage.get(getActivity()).getDays();
        if(mAdapter == null) {
            mAdapter = new DayAdapter(daysList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setDays(daysList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        updateUI();
    }
}
