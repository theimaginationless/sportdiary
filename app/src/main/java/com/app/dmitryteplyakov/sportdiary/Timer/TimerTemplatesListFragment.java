package com.app.dmitryteplyakov.sportdiary.Timer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Timer.Timer;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitry21 on 19.08.17.
 */

public class TimerTemplatesListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TimerAdapter mAdapter;
    private TextView mEmptyTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_programs, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_list_programs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmptyTextView = (TextView) v.findViewById(R.id.fragment_list_programs_empty_text);
        mEmptyTextView.setText(getString(R.string.timer_templates_empty));
        return v;
    }

    private class TimerHolder extends RecyclerView.ViewHolder {
        private Timer mTimer;

        public TimerHolder(View itemView) {
            super(itemView);
        }

        public void bindTimer(Timer timer) {
            mTimer = timer;
        }
    }

    private class TimerAdapter extends RecyclerView.Adapter<TimerHolder> {
        private List<Timer> mTimers;

        public TimerAdapter(List<Timer> timers) {
            mTimers = timers;
        }

        @Override
        public TimerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.programs_list_item, parent, false);
            return new TimerHolder(view);
        }

        @Override
        public void onBindViewHolder(TimerHolder holder, int position) {
            Timer timer = mTimers.get(position);
            holder.bindTimer(timer);
        }

        @Override
        public int getItemCount() {
            return mTimers.size();
        }

        public void setTimers(List<Timer> timers) {
            mTimers = timers;
        }
    }

    private void updateUI() {
        List<Timer> timers = new ArrayList<>();
        if(mAdapter == null) {
            mAdapter = new TimerAdapter(timers);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTimers(timers);
            mAdapter.notifyDataSetChanged();
        }
        if(timers.size() != 0) {

        }
    }
}
