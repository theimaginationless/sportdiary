package com.app.dmitryteplyakov.sportdiary.Weight;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;

/**
 * Created by dmitry21 on 22.08.17.
 */

public class WeightFragment extends Fragment {
    private static final String ARG_WEIGHT_UUID = "com.app.weightfragment.arg_weight_uuid";

    public static WeightFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WEIGHT_UUID, id);
        WeightFragment fragment = new WeightFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(null, container, false);

        return v;
    }
}
