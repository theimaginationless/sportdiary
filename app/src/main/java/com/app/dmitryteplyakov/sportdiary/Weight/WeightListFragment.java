package com.app.dmitryteplyakov.sportdiary.Weight;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    public static final int REQUEST_WEIGHT_EDIT = 18;
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
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentManager manager = getFragmentManager();
                        WeightPickerFragment weightDialog = new WeightPickerFragment();
                        weightDialog.setTargetFragment(WeightListFragment.this, REQUEST_WEIGHT);
                        weightDialog.show(manager, DIALOG_WEIGHT);
                        Log.d("WLF", "=============ADD==========");
                    }
                });
                thread.start();
            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });
        return v;
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
            options = new CharSequence[]{ getString(R.string.edit_training_title_menu), getString(R.string.menu_delete_item) };
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
                mWeightDiffTextView.setVisibility(View.VISIBLE);
                target = sp.getString("weight_target", getString(R.string.lose_weight));
                stringDiff = "0";
                Log.d("WLF", "TARGET: " + sp.getString("weight_target", getString(R.string.lose_weight)));
                if (mWeight.getValue() > weights.get(index + 1).getValue()) {
                    Log.d("WLF", "First: " + Float.toString(mWeight.getValue()) + " PreFirst: " + Float.toString(weights.get(index + 1).getValue()));
                    if (target.equals(getString(R.string.gain_weight))) {
                        mWeightDiffTextView.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_dark));
                        stringDiff = "+" + Float.toString(rounder(mWeight.getValue() - weights.get(index + 1).getValue()));

                    } else if (target.equals(getString(R.string.lose_weight))) {
                        mWeightDiffTextView.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_dark));
                        stringDiff = "+" + Float.toString(rounder(mWeight.getValue() - weights.get(index + 1).getValue()));
                    }

                } else if (mWeight.getValue() < weights.get(index + 1).getValue()) {
                    Log.d("WLF", "First: " + Float.toString(mWeight.getValue()) + " PreFirst: " + Float.toString(weights.get(index + 1).getValue()));
                    if (target.equals(getString(R.string.gain_weight))) {
                        mWeightDiffTextView.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_dark));
                        stringDiff = "-" + Float.toString(rounder(weights.get(index + 1).getValue() - mWeight.getValue()));

                    } else if (target.equals(getString(R.string.lose_weight))) {
                        mWeightDiffTextView.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_dark));
                        stringDiff = "-" + Float.toString(rounder(weights.get(index + 1).getValue() - mWeight.getValue()));
                    }
                } else
                    mWeightDiffTextView.setVisibility(View.GONE);
                Log.d("WLF", stringDiff);
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
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                FragmentManager manager = getFragmentManager();
                                WeightPickerFragment dialog = WeightPickerFragment.getInstance(mWeight.getId());
                                dialog.setTargetFragment(WeightListFragment.this, REQUEST_WEIGHT_EDIT);
                                dialog.show(manager, DIALOG_WEIGHT);
                            }
                        });
                        thread.start();
                    } else if(i == 1) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("WLF", "CLICK" + mWeight.getId().toString());
                                FragmentManager manager = getFragmentManager();
                                DeleteFragment deleteDialog = DeleteFragment.newInstance(mWeight.getId());
                                deleteDialog.setTargetFragment(WeightListFragment.this, REQUEST_WEIGHT_DELETE);
                                deleteDialog.show(manager, DIALOG_WEIGHT_DELETE);
                            }
                        });
                        thread.start();
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateBadge();
            }
        });
    }

    private void updateUI(boolean isAdd, int num) {
        List<Weight> weights = WeightStorage.get(getActivity()).getWeights();
        Log.d("WLF", Integer.toString(weights.size()));
        mAdapter.setWeights(weights);
        if(isAdd) {
            mAdapter.notifyItemInserted(num);
            //mAdapter.notifyItemRangeChanged(num, weights.size() - 1);
            mAdapter.notifyItemRangeChanged(0, 1);
            mRecyclerView.smoothScrollToPosition(0);
        } else {
            mAdapter.notifyItemRemoved(num);
            mAdapter.notifyItemRangeChanged(0, num + 1);
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateBadge();
            }
        });
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
        BottomBarTab weight = mBottomBar.getTabWithId(R.id.action_weight_tab);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> enabledValues = sp.getStringSet("badges_value", new HashSet<String>(Arrays.asList(getString(R.string.action_nutrition_tab_title), getString(R.string.action_weight_tab_title))));

        Boolean isEnabled = sp.getBoolean("switch_on_badges", true);
        if(isEnabled && enabledValues.contains(getString(R.string.action_weight_tab_title))) {
            if (isDoneToday(getActivity())) {
                weight.setBadgeCount(1);
                try {
                    Field badgeFieldDefinition = weight.getClass().getDeclaredField("badge");
                    badgeFieldDefinition.setAccessible(true);
                    TextView badgeTextView = (TextView) badgeFieldDefinition.get(weight);
                    badgeTextView.setText("");
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    Log.d("NDLF", "Exception", e);
                }
            } else
                weight.setBadgeCount(0);
        } else
            Log.d("NDLF", "Badges are disabled!");
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI();
                updateBadge();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("WLF", "RESULT!");
        if(resultCode == Activity.RESULT_OK)
            if (requestCode == REQUEST_WEIGHT) {
                final Weight weight = new Weight((float) data.getSerializableExtra(WeightPickerFragment.EXTRA_NEW_WEIGHT));
                WeightStorage.get(getActivity()).addWeight(weight);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(true, WeightStorage.get(getActivity()).getWeights().indexOf(weight));
                    }
                });
            } else if(requestCode == REQUEST_WEIGHT_DELETE) {
                Log.d("WLF", ((UUID) data.getSerializableExtra(DeleteFragment.EXTRA_RETURN_DELETE_UUID)).toString());
                Weight weight = WeightStorage.get(getActivity()).getWeight((UUID) data.getSerializableExtra(DeleteFragment.EXTRA_RETURN_DELETE_UUID));
                final int num = WeightStorage.get(getActivity()).getWeights().indexOf(weight);
                WeightStorage.get(getActivity()).deleteWeight(weight);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(false, num);
                    }
                });

            } else if(requestCode == REQUEST_WEIGHT_EDIT) {
                Float newValue = ((float) data.getSerializableExtra(WeightPickerFragment.EXTRA_NEW_WEIGHT));
                Weight weight = WeightStorage.get(getActivity()).getWeight((UUID) data.getSerializableExtra(WeightPickerFragment.EXTRA_OLD_WEIGHT));
                weight.setValue(newValue);
                WeightStorage.get(getActivity()).updateWeight(weight);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });

            }
    }
}
