package com.app.dmitryteplyakov.sportdiary.Nutrition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.R;

/**
 * Created by dmitry21 on 12.08.17.
 */

public class NutritionDaysListFragment extends Fragment {
    private TextView mEmptyTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_days_list, container, false);
        mEmptyTextView = (TextView) v.findViewById(R.id.fragment_days_list_empty_text_view);
        mEmptyTextView.setText(getString(R.string.fragment_nutrition_days_empty_text));
        return v;
    }
}
