package com.app.dmitryteplyakov.sportdiary.Nutrition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.R;

import java.util.UUID;

/**
 * Created by dmitry21 on 12.08.17.
 */

public class NewNutritionDayFragment extends Fragment {
    private static final String ARG_NUTRITION_DAY_UUID = "com.app.newnutritiondayfragment.arg_nutrition_day_uuid";
    private TextView mEmptyTextView;

    public static NewNutritionDayFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NUTRITION_DAY_UUID, id);
        NewNutritionDayFragment fragment = new NewNutritionDayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_programs, null);
        mEmptyTextView = (TextView) v.findViewById(R.id.fragment_list_programs_empty_text);
        mEmptyTextView.setText(getString(R.string.fragment_nutrition_days_empty_text));
        return v;
    }

}
