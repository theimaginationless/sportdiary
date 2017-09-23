package com.app.dmitryteplyakov.sportdiary.Timer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Timer.Timer;
import com.app.dmitryteplyakov.sportdiary.Core.Timer.TimerStorage;
import com.app.dmitryteplyakov.sportdiary.Core.TimerTemplate.TimerTemplate;
import com.app.dmitryteplyakov.sportdiary.Core.TimerTemplate.TimerTemplateStorage;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.List;

/**
 * Created by dmitry21 on 23.08.17.
 */

public class TimerDisplayFragment extends Fragment {
    private Spinner mTimerTemplateSpinner;
    private ProgressBar mProgressBar;
    private int mPosition;
    private long mId;
    private CountDownTimer mCountDownTimer;
    private TextView mSeconds;
    private FloatingActionButton mFab;
    private boolean alreadyPaused;
    private long futureMills;
    private long startMills;
    private List<Timer> mTimers;
    private int iterator;
    private int mTimerPosition;
    private int mTimerSetsIterator;
    private int mTimerReplaysIterator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.timer_display, container, false);
        mTimerTemplateSpinner = (Spinner) v.findViewById(R.id.timer_template_spinner);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        mFab = (FloatingActionButton) v.findViewById(R.id.start_timer_fab);
        mSeconds = (TextView) v.findViewById(R.id.seconds_dash);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.timer_title_toolbar));
        List<TimerTemplate> timerTemplates = TimerTemplateStorage.get(getActivity()).getTemplates();
        ArrayAdapter<?> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, timerTemplates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimerTemplateSpinner.setAdapter(adapter);
        mTimerTemplateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                mPosition = position;
                mId = id;
                mTimers = TimerStorage.get(getActivity()).getTimersByParentId(TimerTemplateStorage.get(getActivity()).getTemplates().get(position).getId());
                mTimerSetsIterator = mTimers.get(mPosition).getReplays();
                mTimerReplaysIterator = mTimers.get(mPosition).getReplays();
                mFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPosition = 0;
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextTimer(mPosition);
            }
        });


        return v;
    }

    private void nextTimer(int position) {
        mTimerTemplateSpinner.setEnabled(false);
        mTimerPosition = position;
        iterator = 0;
        Log.d("TDF", "Pos: " + Integer.toString(mTimerPosition));
        startTimer(mTimers.get(position).getTimerValues().get(0) * 1000);
    }

    private void startTimer(final long mills) {
        if(!alreadyPaused)
            startMills = mills;
        Log.d("TDF", "Current: " + Integer.toString(iterator));
        mFab.setImageResource(R.drawable.ic_pause_white_24dp);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });


        mProgressBar.setMax((int) startMills);
        mProgressBar.setProgress((int) (startMills - mills));
        mProgressBar.setIndeterminate(false);
        mCountDownTimer = new CountDownTimer(mills, 10) {
            @Override
            public void onTick(long leftTimeInMills) {
                mProgressBar.setProgress((int) (startMills - leftTimeInMills));
                mSeconds.setText(Long.toString(((leftTimeInMills) / 1000) + 1));
                futureMills = leftTimeInMills;
                //Log.d("TDF", Long.toString(futureMills));
            }

            @Override
            public void onFinish() {
                mSeconds.setText(Integer.toString(0));
                mProgressBar.setProgress((int) (mills + startMills));
                mFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                mFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(alreadyPaused) {
                            alreadyPaused = false;
                            //startTimer(10 * 1000);
                            startTimer(futureMills);
                        } else {
                            //alreadyPaused = false;
                            //startTimer(futureMills);
                        }
                    }
                });
                Log.d("TDF", Integer.toString(mTimerReplaysIterator) + " " + Integer.toString(iterator));
                if((futureMills <= 50) && ((iterator + 1) / 2) < mTimerReplaysIterator) {
                    futureMills = 0;
                    alreadyPaused = false;
                    startTimer(mTimers.get(mTimerPosition).getTimerValues().get((++iterator) % 2) * 1000);
                } else if(mTimerPosition + 1 < mTimers.size()) {
                    nextTimer(++mTimerPosition);
                } else if(mTimerSetsIterator == mTimers.get(mTimerPosition).getReplays()){
                    mTimerTemplateSpinner.setEnabled(true);
                    mTimerPosition = 0;
                    futureMills = 0;
                    iterator = 0;
                    alreadyPaused = false;
                    mFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nextTimer(mPosition);
                        }
                    });
                } else {
                    mTimerPosition = 0;
                    futureMills = 0;
                    iterator = 0;
                    alreadyPaused = false;
                    mTimerSetsIterator++;
                    nextTimer(mPosition);
                }
            }
        };
        mCountDownTimer.start();

    }

    private void pauseTimer() {
        alreadyPaused = true;
        mFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer(futureMills);
            }
        });

        mCountDownTimer.cancel();
    }

    private void stopTimer() {
        alreadyPaused = false;
        mCountDownTimer.cancel();
    }
}
