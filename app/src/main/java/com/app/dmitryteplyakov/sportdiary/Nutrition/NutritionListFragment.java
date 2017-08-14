package com.app.dmitryteplyakov.sportdiary.Nutrition;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.Nutrition;
import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.NutritionStorage;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by dmitry21 on 14.08.17.
 */

public class NutritionListFragment extends Fragment {
    public static final String ARG_NUTRITION_DAY_UUID = "com.app.nutritionlistfragment.arg_nutrition_day_uuid";
    private TextView mEmptyTextView;
    private RecyclerView mRecyclerView;
    private NutritionAdapter mAdapter;
    private FloatingActionButton mFab;

    public static NutritionListFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NUTRITION_DAY_UUID, id);
        NutritionListFragment fragment = new NutritionListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_programs, container, false);
        mEmptyTextView = (TextView) v.findViewById(R.id.fragment_list_programs_empty_text);
        mEmptyTextView.setText(getString(R.string.fragment_new_nutrition_in_day));
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_list_programs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFab = (FloatingActionButton) v.findViewById(R.id.fragment_list_programs_add_program_action_fab);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0)
                    mFab.hide();
                else if(dy < 0)
                    mFab.show();
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nutrition nutrition = new Nutrition();
                NutritionStorage.get(getActivity()).addNutrition(nutrition);
                Intent intent = NutritionActivity.newIntent(getActivity(), nutrition.getId());
                startActivity(intent);
            }
        });
        updateUI();
        return v;
    }

    private class NutritionHolder extends RecyclerView.ViewHolder {
        private Nutrition mNutrition;

        public NutritionHolder(View itemView) {
            super(itemView);
        }

        public void bindNutrition(Nutrition nutrition) {
            mNutrition = nutrition;
        }
    }

    private class NutritionAdapter extends RecyclerView.Adapter<NutritionHolder> {
        List<Nutrition> mNutritions;

        public NutritionAdapter(List<Nutrition> nutritions) {
            mNutritions = nutritions;
        }
        @Override
        public NutritionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.programs_list_item, null);

            return new NutritionHolder(view);
        }
        @Override
        public void onBindViewHolder(NutritionHolder holder, int position) {
            Nutrition nutrition = mNutritions.get(position);
            holder.bindNutrition(nutrition);
        }

        @Override
        public int getItemCount() {
            return mNutritions.size();
        }

        public void setNutritions(List<Nutrition> nutritions) {
            mNutritions = nutritions;
        }
    }

    private void updateUI() {
        List<Nutrition> nutritionList = NutritionStorage.get(getActivity()).getNutritionsByParentDayId((UUID) getArguments().getSerializable(ARG_NUTRITION_DAY_UUID));
        if (mAdapter == null) {
            mAdapter = new NutritionAdapter(nutritionList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setNutritions(nutritionList);
            mAdapter.notifyDataSetChanged();
        }
        if (!mRecyclerView.canScrollVertically(1)) {
            mFab.show();
        }
        if(nutritionList.size() == 0)
            mEmptyTextView.setVisibility(View.GONE);
        else
            mEmptyTextView.setVisibility(mEmptyTextView.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        updateUI();
    }
}
