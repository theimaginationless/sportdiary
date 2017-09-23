package com.app.dmitryteplyakov.sportdiary.Nutrition;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.Nutrition;
import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.NutritionStorage;
import com.app.dmitryteplyakov.sportdiary.Dialogs.DeleteFragment;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by dmitry21 on 14.08.17.
 */

public class NutritionListFragment extends Fragment {
    public static final String ARG_NUTRITION_DAY_UUID = "com.app.nutritionlistfragment.arg_nutrition_day_uuid";
    public static final int REQUEST_DELETE_NUTRITION = 16;
    private static final String DIALOG_DELETE_NUTRITION = "com.app.nutritionlistfragment.dialog_delete_nutrition";
    private TextView mEmptyTextView;
    private RecyclerView mRecyclerView;
    private NutritionAdapter mAdapter;
    private FloatingActionButton mFab;
    private LinearLayoutManager mLinearLayoutManager;

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
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
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
                nutrition.setParentDay((UUID) getArguments().getSerializable(ARG_NUTRITION_DAY_UUID));
                NutritionStorage.get(getActivity()).addNutrition(nutrition);
                Intent intent = NutritionActivity.newIntent(getActivity(), nutrition.getId());
                startActivity(intent);
            }
        });
        updateUI();
        return v;
    }

    private class NutritionHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private Nutrition mNutrition;
        private TextView mNutritionTitle;
        private CharSequence options[];

        public NutritionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mNutritionTitle = itemView.findViewById(R.id.group_name);
            options = new CharSequence[]{getString(R.string.menu_delete_item)};
        }

        public void bindNutrition(Nutrition nutrition) {
            mNutrition = nutrition;
            mNutritionTitle.setText(mNutrition.getProductTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = NutritionActivity.newIntent(getActivity(), mNutrition.getId());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder optionsDialog  = new AlertDialog.Builder(getActivity());
            optionsDialog.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            FragmentManager manager = getFragmentManager();
                            DeleteFragment deleteDialog = DeleteFragment.newInstance(mNutrition.getId());
                            deleteDialog.setTargetFragment(NutritionListFragment.this, REQUEST_DELETE_NUTRITION);
                            deleteDialog.show(manager, DIALOG_DELETE_NUTRITION);
                    }
                }
            });
            optionsDialog.show();
            return true;
        }
    }

    private class NutritionAdapter extends RecyclerView.Adapter<NutritionHolder> {
        List<Nutrition> mNutritions;

        public NutritionAdapter(List<Nutrition> nutritions) {
            mNutritions = nutritions;
        }
        @Override
        public NutritionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.programs_list_item, parent, false);
            return new NutritionHolder(view);
        }
        @Override
        public void onBindViewHolder(NutritionHolder holder, int position) {
            Nutrition nutrition = mNutritions.get(position);
            Log.d("NLF", "TITLE: " + nutrition.getProductTitle());
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
        if(nutritionList.size() != 0)
            mEmptyTextView.setVisibility(View.GONE);
        else
            mEmptyTextView.setVisibility(View.VISIBLE);
        Log.d("NLF", "CALL UPDATE");
    }

    private void updateUI(boolean isAdd, int position) {
        List<Nutrition> nutritionList = NutritionStorage.get(getActivity()).getNutritionsByParentDayId((UUID) getArguments().getSerializable(ARG_NUTRITION_DAY_UUID));
        mAdapter.setNutritions(nutritionList);
        if (isAdd) {
            mAdapter.notifyItemInserted(position);
        } else {
            mAdapter.notifyItemRemoved(position);
        }
        if (!mRecyclerView.canScrollVertically(1)) {
            mFab.show();
        }
        if(nutritionList.size() != 0)
            mEmptyTextView.setVisibility(View.GONE);
        else
            mEmptyTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_DELETE_NUTRITION) {
                UUID nutritionId = (UUID) data.getSerializableExtra(DeleteFragment.EXTRA_RETURN_DELETE_UUID);
                Nutrition nutrition = NutritionStorage.get(getActivity()).getNutrition(nutritionId);
                int num = NutritionStorage.get(getActivity()).getNutritions().indexOf(nutrition);
                NutritionStorage.get(getActivity()).deleteNutrition(nutrition);
                updateUI(false, num);
            }
        }
    }
}
