package com.app.dmitryteplyakov.sportdiary.Nutrition;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
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
import com.app.dmitryteplyakov.sportdiary.Dialogs.DeleteFragment;
import com.app.dmitryteplyakov.sportdiary.GeneralActivity;
import com.app.dmitryteplyakov.sportdiary.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;

import org.w3c.dom.Text;

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
 * Created by dmitry21 on 12.08.17.
 */

public class NutritionDaysListFragment extends Fragment {
    private TextView mEmptyTextView;
    private RecyclerView mRecyclerView;
    private NutritionDayAdapter mAdapter;
    private FloatingActionButton mFab;
    private static final int REQUEST_DELETE_DAY = 15;
    private static final String DIALOG_DELETE_DAY = "com.app.nutritiondayslistfragment.dialog_delete_day";
    private BottomBar mBottomBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_days_list, container, false);
        mBottomBar = (BottomBar) getActivity().findViewById(R.id.bottom_bar);


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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });

        return v;
    }

    private class NutritionDayHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private NutritionDay mNutritionDay;
        private TextView mDateTextView;
        private TextView mSummaryEnergyTextView;
        private TextView mAssociatedDay;
        private CharSequence options[];

        public NutritionDayHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mDateTextView = (TextView) itemView.findViewById(R.id.nutritions_day_list_item_date_text_view);
            mSummaryEnergyTextView = (TextView) itemView.findViewById(R.id.nutritions_day_list_item_summary_energy_text_view);
            mAssociatedDay = (TextView) itemView.findViewById(R.id.nutritions_day_list_item_associated_day_text_view);
            options = new CharSequence[] {getString(R.string.menu_delete_item)};
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
                summaryEnergy += nutrition.getResultEnergy();
            mSummaryEnergyTextView.setText(Integer.toString(summaryEnergy));
        }

        @Override
        public void onClick(View v) {
            Intent intent = NutritionListActivity.newIntent(getActivity(), mNutritionDay.getId());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder optionsDialog = new AlertDialog.Builder(getActivity());
            optionsDialog.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which) {
                        case 0:
                            FragmentManager manager = getFragmentManager();
                            DeleteFragment deleteDialog = DeleteFragment.newInstance(mNutritionDay.getId());
                            deleteDialog.setTargetFragment(NutritionDaysListFragment.this, REQUEST_DELETE_DAY);
                            deleteDialog.show(manager, DIALOG_DELETE_DAY);
                    }
                }
            });
            optionsDialog.show();
            return true;
        }

    }

    public static boolean isDoneToday(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (NutritionDayStorage.get(context).getNutritionDayByDate(calendar.getTime()) == null);
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
        updateBadge();
    }

    private void updateBadge() {
        BottomBarTab nutrition = mBottomBar.getTabWithId(R.id.action_nutrition_tab);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> enabledValues = sp.getStringSet("badges_value", new HashSet<String>(Arrays.asList(getString(R.string.action_nutrition_tab_title), getString(R.string.action_weight_tab_title))));
        Boolean isEnabled = sp.getBoolean("switch_on_badges", true);
        if(isEnabled && enabledValues.contains(getString(R.string.action_nutrition_tab_title))) {
            if (isDoneToday(getActivity())) {
                nutrition.setBadgeCount(1);
                try {
                    Field badgeFieldDefinition = nutrition.getClass().getDeclaredField("badge");
                    badgeFieldDefinition.setAccessible(true);
                    TextView badgeTextView = (TextView) badgeFieldDefinition.get(nutrition);
                    badgeTextView.setText("");
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    Log.d("NDLF", "Exception", e);
                }
            } else
                nutrition.setBadgeCount(0);
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
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_DELETE_DAY) {
                UUID dayId = (UUID) data.getSerializableExtra(DeleteFragment.EXTRA_RETURN_DELETE_UUID);
                NutritionDay day = NutritionDayStorage.get(getActivity()).getNutritionDay(dayId);
                final int num = NutritionDayStorage.get(getActivity()).getNutritionDays().indexOf(day);
                NutritionDayStorage.get(getActivity()).deleteNutritionDay(day);
                NutritionStorage.get(getActivity()).deleteNutritionsByParentDayId(dayId);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(false, num);
                    }
                });

                //Snackbar mSnackBar = Snackbar.make(getActivity().findViewById(R.id.snackbar_place), getString(R.string.snackbar_day_deleted), Snackbar.LENGTH_LONG);
                //mSnackBar.show();
            }
        }
    }
}
