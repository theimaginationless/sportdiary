package com.app.dmitryteplyakov.sportdiary.Nutrition;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Day.DayStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.CompExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.Nutrition;
import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.NutritionStorage;
import com.app.dmitryteplyakov.sportdiary.Core.NutritionDay.NutritionDay;
import com.app.dmitryteplyakov.sportdiary.Core.NutritionDay.NutritionDayStorage;
import com.app.dmitryteplyakov.sportdiary.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by dmitry21 on 12.08.17.
 */

public class NutritionDaysListFragment extends Fragment {
    private TextView mEmptyTextView;
    private RecyclerView mRecyclerView;
    private NutritionDayAdapter mAdapter;
    private FloatingActionButton mFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_days_list, container, false);
        mFab = (FloatingActionButton) getActivity().findViewById(R.id.activity_common_fab_add);
        mEmptyTextView = (TextView) v.findViewById(R.id.fragment_days_list_empty_text_view);
        mEmptyTextView.setText(getString(R.string.fragment_nutrition_days_empty_text));
        mRecyclerView = (RecyclerView) v.findViewById(R.id.training_list_fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    mFab.hide();
                else if(dy < 0)
                    mFab.show();
            }
        });

        updateUI();
        return v;
    }

    private class NutritionDayHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private NutritionDay mNutritionDay;
        private TextView mDateTextView;
        private TextView mSummaryEnergyTextView;
        private TextView mAssociatedDay;

        public NutritionDayHolder(View itemView) {
            super(itemView);
            mDateTextView = (TextView) itemView.findViewById(R.id.nutritions_day_list_item_date_text_view);
            mSummaryEnergyTextView = (TextView) itemView.findViewById(R.id.nutritions_day_list_item_summary_energy_text_view);
            mAssociatedDay = (TextView) itemView.findViewById(R.id.nutritions_day_list_item_associated_day_text_view);
        }

        public void bindNutritionDay(NutritionDay nutritionDay) {
            mNutritionDay = nutritionDay;
            SimpleDateFormat format = new SimpleDateFormat("d MMMM yyyy, HH:mm");
            mDateTextView.setText(format.format(mNutritionDay.getDate()));
            Log.d("NDLF", format.format(mNutritionDay.getDate()));
            if(mNutritionDay.isAssociatedWithDay())
                mAssociatedDay.setText(getString(R.string.nutritions_day_list_item_associated_day) + format.format(DayStorage.get(getActivity()).getDay(mNutritionDay.getAssociatedDay()).getDate()));
            else
                mAssociatedDay.setText(getString(R.string.nutritions_day_list_item_no_associated_day));
            List<Nutrition> nutritionList = NutritionStorage.get(getActivity()).getNutritionsByParentDayId(mNutritionDay.getId());
            int summaryEnergy = 0;
            for(Nutrition nutrition : nutritionList)
                summaryEnergy += nutrition.getEnergy();
            mSummaryEnergyTextView.setText(Integer.toString(summaryEnergy));
        }

        @Override
        public void onClick(View v) {
            //Preparing for running NutritionList
        }

        @Override
        public boolean onLongClick(View v) {
            // Preparing for optionsMenu
            return true;
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

        public void setNutritionDays(List<NutritionDay> nutritionDays) {
            mNutritionDays = nutritionDays;
        }
    }

    private void updateUI() {
        List<NutritionDay> nutritionDaysList;
        nutritionDaysList = NutritionDayStorage.get(getActivity()).getNutritionDays();
        if (mAdapter == null) {
            mAdapter = new NutritionDayAdapter(nutritionDaysList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setNutritionDays(nutritionDaysList);
            mAdapter.notifyDataSetChanged();
        }
        if(nutritionDaysList.size() != 0)
            mEmptyTextView.setVisibility(View.GONE);
        else {
            mEmptyTextView.setVisibility(View.VISIBLE);
            mFab.show();
        }
    }

    private void updateUI(boolean isAdd, int num) {
        List<NutritionDay> nutritionDaysList = NutritionDayStorage.get(getActivity()).getNutritionDays();
        mAdapter.setNutritionDays(nutritionDaysList);
        if(isAdd)
            mAdapter.notifyItemInserted(num);
        else
            mAdapter.notifyItemRemoved(num);
        if(nutritionDaysList.size() != 0)
            mEmptyTextView.setVisibility(View.GONE);
        else {
            mEmptyTextView.setVisibility(View.VISIBLE);
            mFab.show();
        }
        if (!mRecyclerView.canScrollVertically(1)) {
            mFab.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

}
