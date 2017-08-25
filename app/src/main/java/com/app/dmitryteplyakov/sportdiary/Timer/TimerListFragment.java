package com.app.dmitryteplyakov.sportdiary.Timer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Timer.Timer;
import com.app.dmitryteplyakov.sportdiary.Core.Timer.TimerStorage;
import com.app.dmitryteplyakov.sportdiary.Core.TimerTemplate.TimerTemplateStorage;
import com.app.dmitryteplyakov.sportdiary.Dialogs.DeleteFragment;
import com.app.dmitryteplyakov.sportdiary.Dialogs.TitlePickerFragment;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by dmitry21 on 24.08.17.
 */

public class TimerListFragment extends Fragment {
    private static final String ARG_TEMPLATE_UUID = "com.app.timerlistfragment.arg.template.uuid";
    private RecyclerView mRecyclerView;
    private TimerAdapter mAdapter;
    private TextView mEmptyTextView;
    private FloatingActionButton mFab;
    private static final int REQUEST_TIMER_DELETE = 20;
    private static final String DIALOG_DELETE = "com.app.timerlistfragment.dialog_delete";

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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(TimerTemplateStorage.get(getActivity()).getTemplate((UUID) getArguments().getSerializable(ARG_TEMPLATE_UUID)).getTitle());

        mEmptyTextView = (TextView) v.findViewById(R.id.fragment_list_programs_empty_text);
        mEmptyTextView.setText(getString(R.string.timer_empty_text));

        mFab = (FloatingActionButton) v.findViewById(R.id.fragment_list_programs_add_program_action_fab);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0)
                    mFab.hide();
                else if(dy < 0)
                    mFab.show();
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer timer = new Timer();
                timer.setParent((UUID) getArguments().getSerializable(ARG_TEMPLATE_UUID));
                TimerStorage.get(getActivity()).addTimer(timer);
                Intent intent = TimerActivity.newIntent(getActivity(), timer.getId());
                startActivity(intent);
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

    private class TimerHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private Timer mTimer;
        private TextView mTimeSequence;
        private TextView mTitle;
        private CharSequence[] options;

        public TimerHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            options = new CharSequence[] {getString(R.string.menu_delete_item)};
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
            Intent intent = TimerActivity.newIntent(getActivity(), mTimer.getId());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        FragmentManager manager = getFragmentManager();
                        DeleteFragment titleDialog = DeleteFragment.newInstance(mTimer.getId());
                        titleDialog.setTargetFragment(TimerListFragment.this, REQUEST_TIMER_DELETE);
                        titleDialog.show(manager, DIALOG_DELETE);
                    }
                }
            });
            dialog.show();
            return true;
        }
    }

    private class TimerAdapter extends RecyclerView.Adapter<TimerHolder>  {
        List<Timer> mTimerList;

        public TimerAdapter(List<Timer> timers) {
            mTimerList = timers;
        }

        public void setTimers(List<Timer> timers) {
            mTimerList = timers;
        }

        @Override
        public TimerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.timer_list_item, parent, false);
            return new TimerHolder(v);
        }

        @Override
        public void onBindViewHolder(TimerHolder holder, int position) {
            holder.bindTimer(mTimerList.get(position));
        }

        @Override
        public int getItemCount() {
            return mTimerList.size();
        }
    }

    private void updateUI() {
        List<Timer> timers = TimerStorage.get(getActivity()).getTimersByParentId((UUID) getArguments().getSerializable(ARG_TEMPLATE_UUID));
        if(mAdapter == null) {
            mAdapter = new TimerAdapter(timers);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTimers(timers);
            mAdapter.notifyDataSetChanged();
        }
        if(timers.size() != 0)
            mEmptyTextView.setVisibility(View.GONE);
        else
            mEmptyTextView.setVisibility(View.VISIBLE);
    }

    private void updateUI(boolean isAdd, int num) {
        List<Timer> timers = TimerStorage.get(getActivity()).getTimersByParentId((UUID) getArguments().getSerializable(ARG_TEMPLATE_UUID));
        mAdapter.setTimers(timers);
        if(isAdd)
            mAdapter.notifyItemInserted(num);
         else
            mAdapter.notifyItemRemoved(num);
        if(timers.size() != 0)
            mEmptyTextView.setVisibility(View.GONE);
        else
            mEmptyTextView.setVisibility(View.VISIBLE);
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
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_TIMER_DELETE) {
                Timer timer = TimerStorage.get(getActivity()).getTimer((UUID) data.getSerializableExtra(DeleteFragment.EXTRA_RETURN_DELETE_UUID));
                final int num = TimerStorage.get(getActivity()).getTimers().indexOf(timer);
                TimerStorage.get(getActivity()).deleteTimer(timer);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(false, num);
                    }
                });
            }
        }
    }
}
