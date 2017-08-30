package com.app.dmitryteplyakov.sportdiary.Timer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.app.dmitryteplyakov.sportdiary.R;

import java.util.UUID;

/**
 * Created by dmitry21 on 19.08.17.
 */

public class TimerFragment extends Fragment {
    private Button mPreparingTimePicker;
    private Button mWorkingTimePicker;
    private Button mRestTimePicker;
    private Button mIterationsTimePicker;
    private Button mSetsTimePicker;
    private static final String ARG_TIMER_UUID = "com.app.timerfragment.arg_timer_uuid";

    public static Fragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIMER_UUID, id);
        TimerFragment fragment = new TimerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer, parent, false);
        mPreparingTimePicker = (Button) v.findViewById(R.id.preparing_picker);
        mWorkingTimePicker = (Button) v.findViewById(R.id.working_picker);
        mRestTimePicker = (Button) v.findViewById(R.id.rest_picker);
        mIterationsTimePicker = (Button) v.findViewById(R.id.iterations_picker);
        mSetsTimePicker = (Button) v.findViewById(R.id.sets_picker);

        return v;
    }
}
