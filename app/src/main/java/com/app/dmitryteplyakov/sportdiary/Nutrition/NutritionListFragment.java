package com.app.dmitryteplyakov.sportdiary.Nutrition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.dmitryteplyakov.sportdiary.R;

/**
 * Created by dmitry21 on 12.08.17.
 */

public class NutritionListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_common_list_generic, container, false);

        return v;
    }
}
