package com.app.dmitryteplyakov.sportdiary.Timer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import com.app.dmitryteplyakov.sportdiary.R;

/**
 * Created by dmitry21 on 23.08.17.
 */

public class TimerDisplayFragment extends Fragment {
    private Chronometer mCronometer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.timer_display, container, false);
        return v;
    }
}
