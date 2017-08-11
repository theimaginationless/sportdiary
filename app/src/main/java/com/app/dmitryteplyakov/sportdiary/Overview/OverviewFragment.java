package com.app.dmitryteplyakov.sportdiary.Overview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.dmitryteplyakov.sportdiary.R;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class OverviewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        return v;
    }
}
