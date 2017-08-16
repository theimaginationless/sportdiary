package com.app.dmitryteplyakov.sportdiary.Overview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Day.Day;
import com.app.dmitryteplyakov.sportdiary.Core.Day.DayStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.CompExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.Exercise;
import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.Nutrition;
import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.NutritionStorage;
import com.app.dmitryteplyakov.sportdiary.Core.NutritionDay.NutritionDay;
import com.app.dmitryteplyakov.sportdiary.Core.NutritionDay.NutritionDayStorage;
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
    ArrayList<ILineDataSet> lines;
    private TextView mGraphTitle;
    private static boolean isTriggered;
    private static LineDataSet tempSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        mLineChart = (LineChart) v.findViewById(R.id.overview_linechart_nutrition);
        mGraphTitle = (TextView) v.findViewById(R.id.overview_graph_title);

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
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean legendSwitch = sp.getBoolean("switch_on_legend", true);
            mLineChart.getLegend().setEnabled(legendSwitch);
        lines = new ArrayList<>();
        /*if(bSwitch) {
            lines.add(getSecondGraph());
            isTriggered = true;
        }
        lines.add(getFirstGraph());*/
        String mode = sp.getString("graph_mode_list", getString(R.string.nutrition));
        graphEnabler(mode);
        mGraphs = new LineData(lines);
        Log.d("INOF", "CALL");
        mLineChart.setData(mGraphs);
        mLineChart.animateY(600);
        Description description = new Description();
        description.setText(getString(R.string.fragment_program_energy_hint));
        mLineChart.setDescription(description);
        return v;
    }

    public void graphEnabler(String mode) {
        lines = new ArrayList<>();
        mGraphs = new LineData(lines);
        Log.d("OF MODE: ", mode);
        if(mode.equals("")) {
            return;
        }
        if(mode.equals(getString(R.string.nutrition))) {
            lines.add(getFirstGraph());
            mGraphTitle.setText(getString(R.string.overview_fragment_nutrition_stats));
        }
        else if(mode.equals(getString(R.string.training))) {
            lines.add(getSecondGraph());
            mGraphTitle.setText(getString(R.string.overview_fragment_training_stats));
        }
        else if(mode.equals(getString(R.string.combined))) {
            lines.add(getSecondGraph());
            lines.add(getFirstGraph());
            mGraphTitle.setText(getString(R.string.overview_fragment_combined_stats));
        }
        mGraphs = new LineData(lines);
        mLineChart.setData(mGraphs);
    }


    public LineDataSet getFirstGraph() {
        ArrayList<Entry> entries = new ArrayList<>();
        labels = new ArrayList<>();
        List<NutritionDay> nutritionDayList = NutritionDayStorage.get(getActivity()).getNutritionDays();
        Collections.reverse(nutritionDayList);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM");
        boolean skipFlag;
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
        dataset.setFillColor(Color.GREEN);
        dataset.setColor(Color.GREEN);
        dataset.setFillAlpha(100);
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

    public LineDataSet getSecondGraph() {
        ArrayList<Entry> exEntries = new ArrayList<>();
        List<Day> dayList = DayStorage.get(getActivity()).getDays();
        boolean skipFlag;
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
        exDataset.setFillColor(Color.RED);
        exDataset.setColor(Color.RED);
        exDataset.setFillAlpha(100);
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
        mLineChart.getLegend().setEnabled(legendSwitch);
        String mode = sp.getString("graph_mode_list", getString(R.string.nutrition));

        graphEnabler(mode);
        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();
        /*if(bSwitch && !isTriggered) {
            isTriggered = true;
            lines.add(getSecondGraph());
            Collections.reverse(lines);
            mGraphs = new LineData(lines);
            mLineChart.notifyDataSetChanged();
            mLineChart.setData(mGraphs);
            mLineChart.invalidate();
        } else if(!bSwitch && isTriggered) {
            isTriggered = false;
            lines = new ArrayList<>();
            lines.add(getFirstGraph());
            mGraphs = new LineData(lines);
            mLineChart.notifyDataSetChanged();
            mLineChart.setData(mGraphs);
            mLineChart.invalidate();
        }*/
    }
}
