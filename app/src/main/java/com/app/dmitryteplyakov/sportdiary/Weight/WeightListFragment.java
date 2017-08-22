package com.app.dmitryteplyakov.sportdiary.Weight;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Weight.Weight;
import com.app.dmitryteplyakov.sportdiary.Core.Weight.WeightStorage;
import com.app.dmitryteplyakov.sportdiary.Dialogs.DeleteFragment;
import com.app.dmitryteplyakov.sportdiary.Dialogs.WeightPickerFragment;
import com.app.dmitryteplyakov.sportdiary.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by dmitry21 on 22.08.17.
 */

public class WeightListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private BottomBar mBottomBar;
    private WeightAdapter mAdapter;
    private TextView mEmptyTextView;
    public static final int REQUEST_WEIGHT = 16;
    private static final String DIALOG_WEIGHT = "com.app.weightlistfragment.dialog_weight";
    public static final int REQUEST_WEIGHT_DELETE = 17;
    private static final String DIALOG_WEIGHT_DELETE = "com.app.weightlistfragment.dialog_weight_delete";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_days_list, container, false);
        Log.d("WLF", "CREATE");
        mEmptyTextView = (TextView) v.findViewById(R.id.fragment_days_list_empty_text_view);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.training_list_fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFab = (FloatingActionButton) getActivity().findViewById(R.id.activity_common_fab_add);
        mBottomBar = (BottomBar) getActivity().findViewById(R.id.bottom_bar);
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
                FragmentManager manager = getFragmentManager();
                WeightPickerFragment weightDialog = new WeightPickerFragment();
                weightDialog.setTargetFragment(WeightListFragment.this, REQUEST_WEIGHT);
                weightDialog.show(manager, DIALOG_WEIGHT);
            }
        });

        updateUI();
        return v;
    }

    public void shortcutDialog() {
        FragmentManager manager = getFragmentManager();
        WeightPickerFragment weightDialog = new WeightPickerFragment();
        weightDialog.setTargetFragment(WeightListFragment.this, REQUEST_WEIGHT);
        weightDialog.show(manager, DIALOG_WEIGHT);
    }

    private class WeightHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private Weight mWeight;
        private TextView mDateTextView;
        private TextView mWeightTextView;
        private TextView mWeightDiffTextView;
        private CharSequence[] options;

        public WeightHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            mDateTextView = (TextView) itemView.findViewById(R.id.weight_list_item_date);
            mWeightTextView = (TextView) itemView.findViewById(R.id.weight_list_item_value);
            mWeightDiffTextView = (TextView) itemView.findViewById(R.id.weight_list_item_value_diff);
            options = new CharSequence[]{ getString(R.string.menu_delete_item) };
        }

        public void setWeight(Weight weight) {
            mWeight = weight;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM YYYY, HH:mm");
            mDateTextView.setText(dateFormatter.format(mWeight.getDate()));
            mWeightTextView.setText(Float.toString(mWeight.getValue()) + " " + getString(R.string.fragment_program_weight_hint));
            List<Weight> weights = WeightStorage.get(getActivity()).getWeights();
            int index = weights.indexOf(weight);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String target;
            String stringDiff;
            if(index != weights.size() - 1) {

                target = sp.getString("weight_target", getString(R.string.lose_weight));
                stringDiff = "0";
                Log.d("WLF", "TARGET: " + sp.getString("weight_target", getString(R.string.lose_weight)));
                if (mWeight.getValue() > weights.get(index + 1).getValue()) {
                    if (target.equals(getString(R.string.gain_weight))) {
                        mWeightDiffTextView.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_dark));
                        stringDiff = "+" + Float.toString(rounder(mWeight.getValue() - weights.get(index + 1).getValue()));

                    } else if (target.equals(getString(R.string.lose_weight))) {
                        mWeightDiffTextView.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_dark));
                        stringDiff = "+" + Float.toString(rounder(mWeight.getValue() - weights.get(index + 1).getValue()));
                    }

                } else if (mWeight.getValue() < weights.get(index + 1).getValue()) {
                    if (target.equals(getString(R.string.gain_weight))) {
                        mWeightDiffTextView.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_dark));
                        stringDiff = "-" + Float.toString(rounder(weights.get(index + 1).getValue() - mWeight.getValue()));

                    } else if (target.equals(getString(R.string.lose_weight))) {
                        mWeightDiffTextView.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_dark));
                        stringDiff = "-" + Float.toString(rounder(weights.get(index + 1).getValue() - mWeight.getValue()));
                    }
                } else
                    mWeightDiffTextView.setVisibility(View.GONE);
                mWeightDiffTextView.setText(stringDiff + " " + getString(R.string.fragment_program_weight_hint));
            } else
                mWeightDiffTextView.setVisibility(View.GONE);
        }

        private float rounder(float number) {
            return ((float) ((int) Math.round(number * 100))) / 100;
        }


        @Override
        public boolean onLongClick(View v) {
            final AlertDialog.Builder optionsDialog = new AlertDialog.Builder(getActivity());
            optionsDialog.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(i == 0) {
                        Log.d("WLF", "CLICK" + mWeight.getId().toString());
                        FragmentManager manager = getFragmentManager();
                        DeleteFragment deleteDialog = DeleteFragment.newInstance(mWeight.getId());
                        deleteDialog.setTargetFragment(WeightListFragment.this, REQUEST_WEIGHT_DELETE);
                        deleteDialog.show(manager, DIALOG_WEIGHT_DELETE);
                    }
                }
            });
            optionsDialog.show();
            return true;
        }
    }

    private class WeightAdapter extends RecyclerView.Adapter<WeightHolder> {
        private List<Weight> mWeights;

        public WeightAdapter(List<Weight> weights) {
            mWeights = weights;
        }

        public WeightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.weight_list_item, parent, false);

            return new WeightHolder(v);
        }

        public void onBindViewHolder(WeightHolder holder, int position) {
            Weight weight = mWeights.get(position);
            holder.setWeight(weight);

        }

        public int getItemCount() {
            return mWeights.size();
        }

        public void setWeights(List<Weight> weights) {
            mWeights = weights;
        }
    }

    private void updateUI() {
        List<Weight> weights = WeightStorage.get(getActivity()).getWeights();
        if(mAdapter == null) {
            mAdapter = new WeightAdapter(weights);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setWeights(weights);
            mAdapter.notifyDataSetChanged();
        }
        if(weights.size() != 0){
            mEmptyTextView.setVisibility(View.GONE);
        } else {
            mEmptyTextView.setVisibility(View.VISIBLE);
            mFab.show();
        }
        updateBadge();
    }

    private void updateUI(boolean isAdd, int num) {
        List<Weight> weights = WeightStorage.get(getActivity()).getWeights();
        Log.d("WLF", Integer.toString(weights.size()));
        mAdapter.setWeights(weights);
        if(isAdd) {
            mAdapter.notifyItemInserted(num);
            mAdapter.notifyItemRangeChanged(num, weights.size());
        } else {
            mAdapter.notifyItemRemoved(num);
            mAdapter.notifyItemRangeChanged(0, weights.size());
        }
        if(weights.size() != 0){
            mEmptyTextView.setVisibility(View.GONE);
        } else {
            mEmptyTextView.setVisibility(View.VISIBLE);
            mFab.show();
        }
        if(!mRecyclerView.canScrollVertically(1)) { // 1 - down direction
            mFab.show();
        }
        updateBadge();
    }

    public static boolean isDoneToday(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if(WeightStorage.get(context).getWeightsFromDayRange(calendar.getTime()) != null)
            Log.d("WLF", Integer.toString(WeightStorage.get(context).getWeightsFromDayRange(calendar.getTime()).size()));
        return (WeightStorage.get(context).getWeightsFromDayRange(calendar.getTime()) == null);

    }

    private void updateBadge() {
        BottomBarTab nutrition = mBottomBar.getTabWithId(R.id.action_weight_tab);
        if(isDoneToday(getActivity())) {
            nutrition.setBadgeCount(1);
            try {
                Field badgeFieldDefinition = nutrition.getClass().getDeclaredField("badge");
                badgeFieldDefinition.setAccessible(true);
                TextView badgeTextView = (TextView) badgeFieldDefinition.get(nutrition);
                badgeTextView.setText("");
            } catch(NoSuchFieldException | IllegalAccessException e) {
                Log.d("NDLF", "Exception", e);
            }
        } else
            nutrition.setBadgeCount(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("WLF", "RESULT!");
        if(resultCode == Activity.RESULT_OK)
            if (requestCode == REQUEST_WEIGHT) {
                Weight weight = new Weight((float) data.getSerializableExtra(WeightPickerFragment.EXTRA_NEW_WEIGHT));
                WeightStorage.get(getActivity()).addWeight(weight);
                updateUI(true, WeightStorage.get(getActivity()).getWeights().indexOf(weight));
            } else if(requestCode == REQUEST_WEIGHT_DELETE) {
                Log.d("WLF", ((UUID) data.getSerializableExtra(DeleteFragment.EXTRA_RETURN_DELETE_UUID)).toString());
                Weight weight = WeightStorage.get(getActivity()).getWeight((UUID) data.getSerializableExtra(DeleteFragment.EXTRA_RETURN_DELETE_UUID));
                int num = WeightStorage.get(getActivity()).getWeights().indexOf(weight);
                WeightStorage.get(getActivity()).deleteWeight(weight);
                updateUI(false, num);
            }
    }
}
