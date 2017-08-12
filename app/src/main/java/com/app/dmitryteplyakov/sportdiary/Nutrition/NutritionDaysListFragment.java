package com.app.dmitryteplyakov.sportdiary.Nutrition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.NutritionDay.NutritionDay;
import com.app.dmitryteplyakov.sportdiary.Core.NutritionDay.NutritionDayStorage;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.List;

/**
 * Created by dmitry21 on 12.08.17.
 */

public class NutritionDaysListFragment extends Fragment {
    private TextView mEmptyTextView;
    private RecyclerView mRecyclerView;
    private NutritionDayAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_days_list, container, false);
        mEmptyTextView = (TextView) v.findViewById(R.id.fragment_days_list_empty_text_view);
        mEmptyTextView.setText(getString(R.string.fragment_nutrition_days_empty_text));
        mRecyclerView = (RecyclerView) v.findViewById(R.id.training_list_fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return v;
    }

    private class NutritionDayHolder extends RecyclerView.ViewHolder {
        private NutritionDay mNutritionDay;
        public NutritionDayHolder(View itemView) {
            super(itemView);

        }

        public void bindNutritionDay(NutritionDay nutritionDay) {
            mNutritionDay = nutritionDay;
        }
    }

    private class NutritionDayAdapter extends RecyclerView.Adapter<NutritionDayHolder> {
        private List<NutritionDay> mNutritionDays;

        public NutritionDayAdapter(List<NutritionDay> nutritionDays)  {
            mNutritionDays = nutritionDays;
        }

        @Override
        public NutritionDayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.nutritions_day_list_item, parent, false);
            return new NutritionDayHolder(view);
        }

        @Override
        public void onBindViewHolder(NutritionDayHolder holder, int position) {
            NutritionDay nutritionDay = mNutritionDays.get(position);
            holder.bindNutritionDay(nutritionDay);
        }

        @Override
        public int getItemCount() {
            return mNutritionDays.size();
        }

        public void setNutritionDay(List<NutritionDay> nutritionDays) {
            mNutritionDays = nutritionDays;
        }
    }

    private void updateUI() {
        List<NutritionDay> nutritionDayList;
        nutritionDayList = NutritionDayStorage.get(getActivity()).getNutritionDays();
        if (mAdapter == null) {
            mAdapter = new NutritionDayAdapter(nutritionDayList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setNutritionDay(nutritionDayList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

}
