package com.app.dmitryteplyakov.sportdiary.Nutrition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.Nutrition;
import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.NutritionStorage;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.UUID;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class NutritionFragment extends Fragment {
    private static final String ARG_NUTRITION_UUID = "com.app.nutritionfragment.arg_nutrition_uuid";
    private EditText mDishTitle;
    private EditText mEnergy;
    private EditText mWeight;
    private EditText mResultEnergy;
    private Nutrition mNutrition;

    public static NutritionFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NUTRITION_UUID, id);
        NutritionFragment fragment = new NutritionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nutrition, null);
        mNutrition = NutritionStorage.get(getActivity()).getNutrition((UUID) getArguments().getSerializable(ARG_NUTRITION_UUID));
        mDishTitle = (EditText) v.findViewById(R.id.fragment_nutrition_title);
        mEnergy = (EditText) v.findViewById(R.id.fragment_nutrition_energy_edit_text);
        mWeight = (EditText) v.findViewById(R.id.fragment_nutrition_weight_edit_text);
        mResultEnergy = (EditText) v.findViewById(R.id.fragment_nutrition_result_energy_edit_text);
        int count = NutritionStorage.get(getActivity()).getNutritionsByParentDayId(mNutrition.getParentDay()).size() + 1;
        final String title;
        if(mNutrition.getProductTitle() == null)
            title = getString(R.string.no_title) + " " + Integer.toString(count);
        else
            title = mNutrition.getProductTitle();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);

        mDishTitle.setText(mNutrition.getProductTitle());
        if(mNutrition.getEnergy() != 0)
            mEnergy.setText(Integer.toString(mNutrition.getEnergy()));
        if(mNutrition.getWeight() != 0)
            mWeight.setText(Integer.toString(mNutrition.getWeight()));
        if (mNutrition.getResultEnergy() != 0)
            mResultEnergy.setText(Integer.toString(mNutrition.getResultEnergy()));

        mDishTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                if (mNutrition.getProductTitle() == null)
                    mNutrition.setProductTitle(title);
            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if(count == 0)
                    mNutrition.setProductTitle(title);
                else
                    mNutrition.setProductTitle(c.toString());
            }

            @Override
            public void afterTextChanged(Editable c) {
                updateNutrition();
            }
        });

        mEnergy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if(c.toString().equals("")) {
                    mNutrition.setEnergy(0);
                    mNutrition.setResultEnergy(0);
                    mResultEnergy.setText("");
                }
                else {
                    mNutrition.setEnergy(Integer.parseInt(c.toString()));
                    mNutrition.setResultEnergy((int) mNutrition.getWeight() * mNutrition.getEnergy() / 100);
                    mResultEnergy.setText(Integer.toString(mNutrition.getResultEnergy()));
                }
            }

            @Override
            public void afterTextChanged(Editable c) {
                updateNutrition();
            }
        });

        mWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if (c.toString().equals(""))
                    mNutrition.setWeight(0);
                else {
                    mNutrition.setWeight(Integer.parseInt(c.toString()));
                    if(!mResultEnergy.isFocused()) {
                        mNutrition.setResultEnergy( mNutrition.getWeight() * mNutrition.getEnergy() / 100);
                        mResultEnergy.setText(Integer.toString(mNutrition.getResultEnergy()));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable c) {
                updateNutrition();
            }
        });

        mResultEnergy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if (c.toString().equals(""))
                    mNutrition.setResultEnergy(0);
                else {
                    mNutrition.setResultEnergy(Integer.parseInt(c.toString()));
                    if(!mWeight.isFocused() && !mEnergy.isFocused()) {
                        mNutrition.setResultEnergy(Integer.parseInt(c.toString()));
                        Integer weight = 0;
                        if(mNutrition.getEnergy() != 0)
                            weight =  mNutrition.getResultEnergy() * 100 / mNutrition.getEnergy();
                        Log.d("NF", Integer.toString(weight) + " " + Integer.toString(mNutrition.getEnergy()));
                        mNutrition.setWeight(weight);
                        mWeight.setText(Integer.toString(mNutrition.getWeight()));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable c) {
                updateNutrition();
            }
        });

        return v;
    }

    private void updateNutrition() {
        NutritionStorage.get(getActivity()).updateNutrition(mNutrition);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mNutrition.getProductTitle() == null)
            mNutrition.setProductTitle(getString(R.string.no_title) + " " + Integer.toString(NutritionStorage.get(getActivity()).getNutritionsByParentDayId(mNutrition.getParentDay()).size() + 1));
        updateNutrition();
    }
}
