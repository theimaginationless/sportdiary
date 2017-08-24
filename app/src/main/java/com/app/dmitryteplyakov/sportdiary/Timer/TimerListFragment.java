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

import java.util.UUID;

/**
 * Created by dmitry21 on 24.08.17.
 */

public class TimerListFragment extends Fragment {
    private static final String ARG_TEMPLATE_UUID = "com.app.timerlistfragment.arg.template.uuid";
    private RecyclerView mRecyclerView;

    public static TimerListFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TEMPLATE_UUID, id);
        TimerListFragment fragment = new TimerListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_programs, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_list_programs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    private class TimerHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private Timer mTimer;
        private TextView mTimeSequence;
        private TextView mTitle;

        public TimerHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mTitle = (TextView) itemView.findViewById(R.id.timer_name);
            mTimeSequence = (TextView) itemView.findViewById(R.id.time_sequence_item);
        }

        public void bindTimer(Timer timer) {
            mTimer = timer;
            mTitle.setText(mTimer.getTitle());
            mTimeSequence.setText(Integer.toString(mTimer.getPreparing()) + "-" + Integer.toString(mTimer.getWorkout()) + "-" + Integer.toString(mTimer.getRest()) + "-" + Integer.toString(mTimer.getCalmDown()));
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {

            return true;
        }
    }
}
