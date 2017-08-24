package com.app.dmitryteplyakov.sportdiary.Overview;

import android.content.SharedPreferences;
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
import com.app.dmitryteplyakov.sportdiary.Core.Weight.Weight;
import com.app.dmitryteplyakov.sportdiary.Core.Weight.WeightStorage;
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
    private LineData mWeightGraph;
    private ArrayList<ILineDataSet> lines;
    private ArrayList<ILineDataSet> linesWeight;
    private TextView mGraphTitle;
    private CardView mGraphCardView;
    private TextView mDaysCount;
    private TextView mDiffWeight;
    private LinearLayout mlinearlayoutLastDiff;
    private CardView mSummaryTrainingCardView;
    private LineChart mWeightLineChart;
    private CardView mGraphWeightCardView;
    private float mWeightMaxY;
    private float mFirstTrainingMaxY;
    private float mSecondTrainingMaxY;
    private float mCommonTrainingPlotMaxY;
    private float mOffsetYAxis;


    private void drawWeightCard(View v, int color) {
        if(color == 0)
            color = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        linesWeight = new ArrayList<>();
        mWeightLineChart = (LineChart) v.findViewById(R.id.overview_linechart_weight);
        mWeightLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mWeightLineChart.getAxisRight().setDrawGridLines(false);
        mWeightLineChart.setScaleEnabled(false);
        mWeightLineChart.setDrawGridBackground(false);
        mWeightLineChart.getXAxis().setDrawAxisLine(false);
        mWeightLineChart.getAxisLeft().setEnabled(false);
        mWeightLineChart.getAxisLeft().setDrawAxisLine(false);
        mWeightLineChart.getAxisLeft().setDrawGridLines(false);
        mWeightLineChart.getAxisRight().setEnabled(false);
        mWeightLineChart.getXAxis().setGranularity(1f);
        mWeightLineChart.getXAxis().setGranularityEnabled(true);
        mWeightLineChart.getAxisLeft().setGranularityEnabled(true);
        mWeightLineChart.getAxisLeft().setGranularity(1f);
        mWeightLineChart.setNoDataText(getString(R.string.overview_fragment_no_data_for_graph));
        mWeightLineChart.getXAxis().setDrawGridLines(false);
        mWeightLineChart.getLegend().setEnabled(false);
        mWeightLineChart.setTouchEnabled(false);

        //mWeightLineChart.getAxisLeft().setAxisMaximum(100);
        mWeightLineChart.setAutoScaleMinMaxEnabled(true);

        linesWeight.add(getWeightGraph(255, color));
        if(mWeightMaxY != 0)
            mWeightLineChart.getAxisLeft().setAxisMaximum(mWeightMaxY + mOffsetYAxis);
        Description desc = new Description();
        desc.setText(getString(R.string.fragment_program_weight_hint));
        desc.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        mWeightLineChart.setDescription(desc);
        mWeightGraph = new LineData(linesWeight);
        mWeightLineChart.setData(mWeightGraph);
        mWeightLineChart.animateY(600);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        mOffsetYAxis = 20;
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mGraphWeightCardView = (CardView) v.findViewById(R.id.overview_linechart_weight_host_card_view);
        mGraphWeightCardView = (CardView) v.findViewById(R.id.overview_linechart_weight_host_card_view);
        mGraphCardView = (CardView) v.findViewById(R.id.overview_linechart_nutrition_host_card_view);
        mLineChart = (LineChart) v.findViewById(R.id.overview_linechart_nutrition);
        mGraphTitle = (TextView) v.findViewById(R.id.overview_graph_title);
        mDiffWeight = (TextView) v.findViewById(R.id.last_diff_training);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        mLineChart.setTouchEnabled(false);

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
        if (DayStorage.get(getActivity()).getDays().size() != 0) {
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
                    lastAverageWeight = ((float) ((int) Math.round((sum / count) * 100))) / 100;
                if (preCount != 0)
                    preLastAverageWeight = ((float) ((int) Math.round((preSum / preCount) * 100))) / 100;
            }
            String diffWeight;
            if (lastAverageWeight > preLastAverageWeight) {
                mDiffWeight.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_dark));
                diffWeight = "+" + Float.toString(((float) Math.round((lastAverageWeight - preLastAverageWeight) * 100)) / 100) + " " + getString(R.string.fragment_program_weight_hint);
            } else if (lastAverageWeight < preLastAverageWeight) {
                mDiffWeight.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_dark));
                diffWeight = "-" + Float.toString(((float) Math.round((preLastAverageWeight - lastAverageWeight) * 100)) / 100) + " " + getString(R.string.fragment_program_weight_hint);
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


        boolean graphIsEnabled = sp.getBoolean("switch_on_graphs", true);
        if (!graphIsEnabled)
            mGraphCardView.setVisibility(View.GONE);
        boolean legendSwitch = sp.getBoolean("switch_on_legend", true);
        mLineChart.getLegend().setEnabled(legendSwitch);
        lines = new ArrayList<>();
        String mode = sp.getString("graph_mode_list", getString(R.string.combined));
        String overlappingSwap = sp.getString("overlappingSwap", getString(R.string.switch_overlapping_graphs_first_over_second));
        //graphEnabler(mode, overlappingSwap);
        mGraphs = new LineData(lines);
        mLineChart.setData(mGraphs);
        mLineChart.animateY(600);
        Description description = new Description();
        description.setText(getString(R.string.fragment_program_energy_hint));
        description.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        mLineChart.setDescription(description);
        return v;
    }


    public void graphEnabler(String mode, String overlappingSwap, int color) {
        lines = new ArrayList<>();
        mGraphs = new LineData(lines);
        int fullAlpha = 255;
        int partAlpha = 200;
        mCommonTrainingPlotMaxY = 0;

        if(mFirstTrainingMaxY > mSecondTrainingMaxY)
            mCommonTrainingPlotMaxY = mFirstTrainingMaxY;
        else if(mFirstTrainingMaxY < mSecondTrainingMaxY)
            mCommonTrainingPlotMaxY = mSecondTrainingMaxY;

        Log.d("OF MODE: ", mode);
        if (mode.equals("")) {
            return;
        }
        if (mode.equals(getString(R.string.nutrition))) {
            lines.add(getFirstGraph(fullAlpha, color));
            mGraphTitle.setText(getString(R.string.overview_fragment_nutrition_stats));
        } else if (mode.equals(getString(R.string.training))) {
            lines.add(getSecondGraph(fullAlpha, color));
            mGraphTitle.setText(getString(R.string.overview_fragment_training_stats));
        } else {
            if (overlappingSwap.equals(getString(R.string.switch_overlapping_graphs_second_over_first))) {
                lines.add(getFirstGraph(fullAlpha, color));
                lines.add(getSecondGraph(partAlpha, color));
            } else {
                lines.add(getSecondGraph(fullAlpha, color));
                lines.add(getFirstGraph(partAlpha, color));
            }
            mGraphTitle.setText(getString(R.string.overview_fragment_combined_stats));
        }
        mGraphs = new LineData(lines);
        if(mCommonTrainingPlotMaxY != 0)
            mLineChart.getAxisLeft().setAxisMaximum(mCommonTrainingPlotMaxY + mOffsetYAxis);
        mLineChart.setData(mGraphs);
    }

    public LineDataSet getWeightGraph(int alpha, int color) {
        ArrayList<Entry> entries = new ArrayList<>();
        labels = new ArrayList<>();
        List<Weight> weights = WeightStorage.get(getActivity()).getWeights();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM");
        boolean skipFlag;
        //int color = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        mWeightMaxY = 0;
        for (int j = 0; j < 7; j++) {
            skipFlag = false;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int day = calendar.get(Calendar.DAY_OF_MONTH) - j;
            calendar.set(Calendar.DAY_OF_MONTH, day);
            Calendar date = Calendar.getInstance();
            for (Weight weight : weights) {
                date.setTime(weight.getDate());
                if(mWeightMaxY < weight.getValue())
                    mWeightMaxY = weight.getValue();

                if (date.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                    labels.add(dateFormatter.format(weight.getDate()));
                    entries.add(new Entry(6 - j, weight.getValue()));
                    skipFlag = true;
                    break;
                }
            }
            if (skipFlag)
                continue;
            labels.add(dateFormatter.format(calendar.getTime()));
            entries.add(new Entry(6 - j, 0));
        }
        Collections.reverse(entries);
        Collections.reverse(labels);
        LineDataSet dataset = new LineDataSet(entries, getString(R.string.action_weight_tab_title));
        dataset.setFillColor(color);
        dataset.setColor(color);
        dataset.setCircleColorHole(color);
        dataset.setFillAlpha(alpha);
        dataset.setHighlightEnabled(false);
        dataset.setValueTextSize(10f);
        dataset.setDrawFilled(true);

        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        mWeightLineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axisBase) {
                if (value < 0)
                    return "";
                else if (value == 0) {
                    return labels.get(0);
                } else {
                    return labels.get((int) (value));
                }
            }
        });
        return dataset;
    }


    public LineDataSet getFirstGraph(int alpha, int color) {
        ArrayList<Entry> entries = new ArrayList<>();
        labels = new ArrayList<>();
        List<NutritionDay> nutritionDayList = NutritionDayStorage.get(getActivity()).getNutritionDays();
        Collections.reverse(nutritionDayList);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM");
        mFirstTrainingMaxY = 0;
        boolean skipFlag;
        if(color == 0)
            color = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        for (int j = 0; j < 7; j++) {
            skipFlag = false;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int day = calendar.get(Calendar.DAY_OF_MONTH) - j;
            calendar.set(Calendar.DAY_OF_MONTH, day);
            Calendar date = Calendar.getInstance();
            for (NutritionDay nDay : nutritionDayList) {
                date.setTime(nDay.getDate());
                if (date.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                    labels.add(dateFormatter.format(nDay.getDate()));
                    int resultEnergy = 0;
                    for (Nutrition nutrition : NutritionStorage.get(getActivity()).getNutritionsByParentDayId(nDay.getId())) {
                        resultEnergy += nutrition.getResultEnergy();
                    }
                    if(mFirstTrainingMaxY < resultEnergy)
                        mFirstTrainingMaxY = resultEnergy;

                    entries.add(new Entry(6 - j, resultEnergy));
                    skipFlag = true;
                }
            }
            if (skipFlag)
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
                if (value < 0)
                    return "";
                else if (value == 0) {
                    return labels.get(0);
                } else {
                    return labels.get((int) (value));
                }
            }
        });
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        return dataset;
    }

    public LineDataSet getSecondGraph(int alpha, int color) {
        ArrayList<Entry> exEntries = new ArrayList<>();
        List<Day> dayList = DayStorage.get(getActivity()).getDays();
        mSecondTrainingMaxY = 0;
        boolean skipFlag;
        if(color == 0)
            color = ContextCompat.getColor(getActivity(), R.color.colorAccent);
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
                    if(mSecondTrainingMaxY < resultEnergy)
                        mSecondTrainingMaxY = resultEnergy;

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
        boolean legendSwitch = sp.getBoolean("switch_on_legend", true);
        boolean graphIsEnabled = sp.getBoolean("switch_on_graphs", true);
        String mode = sp.getString("graph_mode_list", getString(R.string.combined));
        String overlappingSwap = sp.getString("overlappingSwap", getString(R.string.switch_overlapping_graphs_first_over_second));
        String colorMode = sp.getString("graph_color_list", getString(R.string.color_indigo));
        String colorModeMulti = sp.getString("graph_color_list_multi", getString(R.string.color_indigo));
        int color = 0;
        int multiGraphColor = 0;
        mLineChart.getLegend().setEnabled(legendSwitch);

        if (!graphIsEnabled)
            mGraphCardView.setVisibility(View.GONE);
        else {
            mGraphCardView.setVisibility(View.VISIBLE);
            if(!mode.equals(getString(R.string.combined))) {
                if (colorModeMulti.equals(getString(R.string.color_indigo))) // MultiGraphs Color Settings
                    multiGraphColor = ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark);
                else if (colorModeMulti.equals(getString(R.string.color_indigo_light)))
                    multiGraphColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
                else if (colorModeMulti.equals(getString(R.string.color_pink)))
                    multiGraphColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);
            }

            graphEnabler(mode, overlappingSwap, multiGraphColor);
            Log.d("OF", "COLOR: " + Integer.toString(multiGraphColor));
            mLineChart.notifyDataSetChanged();
            mLineChart.invalidate();
        }
        if (!sp.getBoolean("switch_on_graph_weight", true))
            mGraphWeightCardView.setVisibility(View.GONE);
        else {
            mGraphWeightCardView.setVisibility(View.VISIBLE);
                if (colorMode.equals(getString(R.string.color_indigo))) // WeightGraph Color Settings
                    color = ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark);
                else if (colorMode.equals(getString(R.string.color_indigo_light)))
                    color = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
                else if (colorMode.equals(getString(R.string.color_pink)))
                    color = ContextCompat.getColor(getActivity(), R.color.colorAccent);
            }
        drawWeightCard(getView(), color);
    }
}
