package com.app.dmitryteplyakov.sportdiary.Nutrition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.dmitryteplyakov.sportdiary.R;

import java.util.UUID;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class NutritionFragment extends Fragment {
    private static final String ARG_NUTRITION_DAY_ID = "com.app.nutritionfragment.arg_nutrition_day_id";

    public static NutritionFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NUTRITION_DAY_ID, id);
        NutritionFragment fragment = new NutritionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_nutrition, null);

        return v;
    }
}
