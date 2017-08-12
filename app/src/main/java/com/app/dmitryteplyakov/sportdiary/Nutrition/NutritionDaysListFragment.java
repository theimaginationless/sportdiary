package com.app.dmitryteplyakov.sportdiary.Nutrition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.Nutrition;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.List;

/**
 * Created by dmitry21 on 12.08.17.
 */

public class NutritionDaysListFragment extends Fragment {
    private TextView mEmptyTextView;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_days_list, container, false);
        mEmptyTextView = (TextView) v.findViewById(R.id.fragment_days_list_empty_text_view);
        mEmptyTextView.setText(getString(R.string.fragment_nutrition_days_empty_text));
        mRecyclerView = (RecyclerView) v.findViewById(R.id.training_list_fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    private class NutritionHolder extends RecyclerView.ViewHolder {
        private Nutrition mNutritionDay;
        public NutritionHolder(View itemView) {
            super(itemView);

        }

        public void bindNutrition(Nutrition nutritionDay) {
            mNutritionDay = nutritionDay;
        }
    }

    private class NutritionAdapter extends RecyclerView.Adapter<NutritionHolder> {
        private List<Nutrition> mNutritionDays;

        public NutritionAdapter(List<Nutrition> nutritionDays)  {
            mNutritionDays = nutritionDays;
        }

        @Override
        public NutritionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.nutritions_day_list_item, parent, false);
            return new NutritionHolder(view);
        }

        @Override
        public void onBindViewHolder(NutritionHolder holder, int position) {
            Nutrition nutritionDay = mNutritionDays.get(position);
            holder.bindNutrition(nutritionDay);
        }

        @Override
        public int getItemCount() {
            return mNutritionDays.size();
        }

        public void setNutrtitions(List<Nutrition> nutritionDays) {
            mNutritionDays = nutritionDays;
        }
    }

    private void updateUI() {
        List<Nutrition> nutritionDaysList;

    }

}
