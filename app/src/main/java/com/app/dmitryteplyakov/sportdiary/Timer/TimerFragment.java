package com.app.dmitryteplyakov.sportdiary.Timer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.app.dmitryteplyakov.sportdiary.R;

/**
 * Created by dmitry21 on 19.08.17.
 */

public class TimerFragment extends Fragment {
    private NumberPicker mSecondPicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer, parent, false);
        mSecondPicker = (NumberPicker) v.findViewById(R.id.number_picker);
        mSecondPicker.setMinValue(0);
        return v;
    }
}
