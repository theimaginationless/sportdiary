package com.app.dmitryteplyakov.sportdiary.Overview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Day.Day;
import com.app.dmitryteplyakov.sportdiary.Core.Day.DayStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.CompExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.Exercise;
import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.Nutrition;
import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.NutritionStorage;
import com.app.dmitryteplyakov.sportdiary.Core.NutritionDay.NutritionDay;
import com.app.dmitryteplyakov.sportdiary.Core.NutritionDay.NutritionDayStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Training.Training;
import com.app.dmitryteplyakov.sportdiary.Core.Training.TrainingStorage;
import com.app.dmitryteplyakov.sportdiary.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class OverviewFragment extends Fragment {
    private LineChart mLineChart;
    private ArrayList<String> labels;
    private SharedPreferences sp;
    private LineData mGraphs;
    private ArrayList<ILineDataSet> lines;
    private TextView mGraphTitle;
    private static boolean isTriggered;
    private static LineDataSet tempSet;
    private CardView mGraphCardView;
    private TextView mDaysCount;
    private TextView mDiffWeight;
    private LinearLayout mlinearlayoutLastDiff;
    private CardView mSummaryTrainingCardView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        mGraphCardView = (CardView) v.findViewById(R.id.overview_linechart_nutrition_host_card_view);
        mLineChart = (LineChart) v.findViewById(R.id.overview_linechart_nutrition);
        mGraphTitle = (TextView) v.findViewById(R.id.overview_graph_title);
        mDiffWeight = (TextView) v.findViewById(R.id.last_diff_training);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.getAxisRight().setDrawGridLines(false);
        mLineChart.setScaleEnabled(false);
        mLineChart.setDrawGridBackground(false);
        mLineChart.getXAxis().setDrawAxisLine(false);
        mLineChart.getAxisLeft().setEnabled(false);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getXAxis().setGranularity(1f);
        mLineChart.getXAxis().setGranularityEnabled(true);
        mLineChart.getAxisLeft().setGranularityEnabled(true);
        mLineChart.getAxisLeft().setGranularity(1f);
        mLineChart.setNoDataText(getString(R.string.overview_fragment_no_data_for_graph));
        mLineChart.getXAxis().setDrawGridLines(false);
        mDaysCount = (TextView) v.findViewById(R.id.count_days);
        mDaysCount.setText(getResources().getQuantityString(R.plurals.days, DayStorage.get(getActivity()).getDays().size(), DayStorage.get(getActivity()).getDays().size()));
        mlinearlayoutLastDiff = (LinearLayout) v.findViewById(R.id.linearlayout_last_diff);
        mSummaryTrainingCardView = (CardView) v.findViewById(R.id.overview_summary_training_cardview);
        if(DayStorage.get(getActivity()).getDays().size() != 0 ) {
            Day day = DayStorage.get(getActivity()).getDays().get(0);
            Training lastTraining = TrainingStorage.get(getActivity()).getTraining(day.getTrainingId());
            Training preLastTraining = null;
            List<Day> days = DayStorage.get(getActivity()).getDays();
            days.remove(0);
            for (Day lDay : days) {
                Training training = TrainingStorage.get(getActivity()).getTraining(lDay.getTrainingId());
                if (training != null) {
                    if (training.equals(lastTraining)) {
                        preLastTraining = training;
                        break;
                    }
                }

            }
            float lastAverageWeight = 0;
            float preLastAverageWeight = 0;
            float sum = 0;
            int count = 0;
            float preSum = 0;
            int preCount = 0;
            if (preLastTraining != null) {
                for (Exercise exercise : CompExerciseStorage.get(getActivity()).getExercisesByParentId(lastTraining.getId())) {
                    if (exercise.getParentDayId().equals(day.getId())) {
                        sum += exercise.getWeight();
                        count++;
                    } else if (exercise.getParentDayId().equals(days.get(0).getId())) {
                        preSum += exercise.getWeight();
                        preCount++;
                    }
                }
                if (count != 0)
                    lastAverageWeight = ((float) ((int) ((sum / count) * 100)) / 100);
                if (preCount != 0)
                    preLastAverageWeight = ((float) ((int) ((preSum / preCount) * 100)) / 100);
            }
            String diffWeight;
            if (lastAverageWeight > preLastAverageWeight) {
                mDiffWeight.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_dark));
                diffWeight = "+" + Float.toString(lastAverageWeight - preLastAverageWeight) + " " + getString(R.string.fragment_program_weight_hint);
            } else if (lastAverageWeight < preLastAverageWeight) {
                mDiffWeight.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_dark));
                diffWeight = "-" + Float.toString(preLastAverageWeight - lastAverageWeight) + " " + getString(R.string.fragment_program_weight_hint);
            } else {
                diffWeight = "0" + " " + getString(R.string.fragment_program_weight_hint);
                mDiffWeight.setVisibility(View.GONE);
                mlinearlayoutLastDiff.setVisibility(View.GONE);
            }
            mDiffWeight.setText(diffWeight);

        } else {
            mDiffWeight.setVisibility(View.GONE);
            mlinearlayoutLastDiff.setVisibility(View.GONE);
            mSummaryTrainingCardView.setVisibility(View.GONE);
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean graphIsEnabled = sp.getBoolean("switch_on_graphs", true);
        if(!graphIsEnabled)
            mGraphCardView.setVisibility(View.GONE);
        boolean legendSwitch = sp.getBoolean("switch_on_legend", true);
        mLineChart.getLegend().setEnabled(legendSwitch);
        lines = new ArrayList<>();
        String mode = sp.getString("graph_mode_list", getString(R.string.combined));
        String overlappingSwap = sp.getString("overlappingSwap", getString(R.string.switch_overlapping_graphs_first_over_second));
        graphEnabler(mode, overlappingSwap);
        mGraphs = new LineData(lines);
        Log.d("INOF", "CALL");
        mLineChart.setData(mGraphs);
        mLineChart.animateY(600);
        Description description = new Description();
        description.setText(getString(R.string.fragment_program_energy_hint));
        mLineChart.setDescription(description);
        return v;
    }

    public void graphEnabler(String mode, String overlappingSwap) {
        lines = new ArrayList<>();
        mGraphs = new LineData(lines);
        int fullAlpha = 255;
        int partAlpha = 200;
        Log.d("OF MODE: ", mode);
        if(mode.equals("")) {
            return;
        }
        if(mode.equals(getString(R.string.nutrition))) {
            lines.add(getFirstGraph(fullAlpha));
            mGraphTitle.setText(getString(R.string.overview_fragment_nutrition_stats));
        }
        else if(mode.equals(getString(R.string.training))) {
            lines.add(getSecondGraph(fullAlpha));
            mGraphTitle.setText(getString(R.string.overview_fragment_training_stats));
        }
        else {
            if(overlappingSwap.equals(getString(R.string.switch_overlapping_graphs_second_over_first))) {
                lines.add(getFirstGraph(fullAlpha));
                lines.add(getSecondGraph(partAlpha));
            } else {
                lines.add(getSecondGraph(fullAlpha));
                lines.add(getFirstGraph(partAlpha));
            }
            mGraphTitle.setText(getString(R.string.overview_fragment_combined_stats));
        }
        mGraphs = new LineData(lines);
        mLineChart.setData(mGraphs);
    }


    public LineDataSet getFirstGraph(int alpha) {
        ArrayList<Entry> entries = new ArrayList<>();
        labels = new ArrayList<>();
        List<NutritionDay> nutritionDayList = NutritionDayStorage.get(getActivity()).getNutritionDays();
        Collections.reverse(nutritionDayList);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM");
        boolean skipFlag;
        int color = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        for(int j = 0; j < 7; j++) {
            skipFlag = false;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int day = calendar.get(Calendar.DAY_OF_MONTH) - j;
            calendar.set(Calendar.DAY_OF_MONTH, day);
            Calendar date = Calendar.getInstance();
            for (NutritionDay nDay : nutritionDayList) {
                date.setTime(nDay.getDate());
                if(date.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                    labels.add(dateFormatter.format(nDay.getDate()));
                    int resultEnergy = 0;
                    for (Nutrition nutrition : NutritionStorage.get(getActivity()).getNutritionsByParentDayId(nDay.getId())) {
                        resultEnergy += nutrition.getResultEnergy();
                    }
                    entries.add(new Entry(6 - j, resultEnergy));
                    skipFlag = true;
                }
            }
            if(skipFlag)
                continue;
            labels.add(dateFormatter.format(calendar.getTime()));
            entries.add(new Entry(6 - j, 0));
        }
        Collections.reverse(entries);
        Collections.reverse(labels);

        LineDataSet dataset = new LineDataSet(entries, getString(R.string.action_nutrition_tab_title));
        dataset.setFillColor(color);
        dataset.setColor(color);
        dataset.setCircleColorHole(color);
        dataset.setFillAlpha(alpha);
        dataset.setHighlightEnabled(false);
        dataset.setValueTextSize(10f);
        dataset.setDrawFilled(true);
        mLineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axisBase) {
                if(value < 0)
                    return "";
                else if(value == 0) {
                    return labels.get(0);
                } else {
                    return labels.get((int) (value));
                }
            }
        });
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        return dataset;
    }

    public LineDataSet getSecondGraph(int alpha) {
        ArrayList<Entry> exEntries = new ArrayList<>();
        List<Day> dayList = DayStorage.get(getActivity()).getDays();
        boolean skipFlag;
        int color = ContextCompat.getColor(getActivity(), R.color.colorAccent);
        for (int j = 0; j < 7; j++) {
            skipFlag = false;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int iday = calendar.get(Calendar.DAY_OF_MONTH) - j;
            calendar.set(Calendar.DAY_OF_MONTH, iday);
            Calendar date = Calendar.getInstance();
            for (Day day : dayList) {
                date.setTime(day.getDate());
                if (date.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                    int resultEnergy = 0;
                    for (Exercise exercise : CompExerciseStorage.get(getActivity()).getExercisesByParentTrainingDayId(day.getId())) {
                        resultEnergy += exercise.getEnergy();
                    }
                    exEntries.add(new Entry(6 - j, resultEnergy));
                    skipFlag = true;
                }
            }
            if (skipFlag)
                continue;
            exEntries.add(new Entry(6 - j, 0));
        }
        Collections.reverse(exEntries);
        LineDataSet exDataset = new LineDataSet(exEntries, getString(R.string.action_training_tab_title));
        exDataset.setFillColor(color);
        exDataset.setColor(color);
        exDataset.setFillAlpha(alpha);
        exDataset.setCircleColorHole(color);
        exDataset.setHighlightEnabled(false);
        exDataset.setValueTextSize(10f);
        exDataset.setDrawFilled(true);
        exDataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        return exDataset;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean legendSwitch = sp.getBoolean("switch_on_legend", true);
        boolean graphIsEnabled = sp.getBoolean("switch_on_graphs", true);
        mLineChart.getLegend().setEnabled(legendSwitch);
        String mode = sp.getString("graph_mode_list", getString(R.string.combined));
        String overlappingSwap = sp.getString("overlappingSwap", getString(R.string.switch_overlapping_graphs_first_over_second));
        if(!graphIsEnabled)
            mGraphCardView.setVisibility(View.GONE);
        else
            mGraphCardView.setVisibility(View.VISIBLE);
        graphEnabler(mode, overlappingSwap);
        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();

    }
}
