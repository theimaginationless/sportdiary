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
    boolean isTriggered;
    private static LineDataSet tempSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        mLineChart = (LineChart) v.findViewById(R.id.overview_linechart_nutrition);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.getAxisRight().setDrawGridLines(false);
        mLineChart.setScaleEnabled(false);
        mLineChart.setDrawGridBackground(false);
        mLineChart.getXAxis().setDrawAxisLine(false);
        mLineChart.getAxisLeft().setEnabled(false);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getLegend().setEnabled(false);
        mLineChart.getXAxis().setGranularity(1f);
        mLineChart.getXAxis().setGranularityEnabled(true);
        mLineChart.getAxisLeft().setGranularityEnabled(true);
        mLineChart.getAxisLeft().setGranularity(1f);
        mLineChart.setNoDataText(getString(R.string.overview_fragment_no_data_for_graph));
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean bSwitch = sp.getBoolean("switch_overlay_exercise_on_graph", false);
        Log.d("OF", Boolean.toString(bSwitch));
        lines = new ArrayList<>();

            lines.add(getFirstGraph());
        if(bSwitch)
            lines.add(getSecondGraph());

            mGraphs = new LineData(lines);
        Log.d("INOF", "CALL");
            mLineChart.setData(mGraphs);
            mLineChart.animateY(600);
            Description description = new Description();
            description.setText(getString(R.string.fragment_program_energy_hint));
            mLineChart.setDescription(description);
        return v;
    }

    public LineDataSet getFirstGraph() {
        ArrayList<Entry> entries = new ArrayList<>();
        labels = new ArrayList<>();
        List<NutritionDay> nutritionDayList = NutritionDayStorage.get(getActivity()).getNutritionDays();
        Collections.reverse(nutritionDayList);
        //

        //
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM");
        int count = 0;
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
                    count++;
                }
            }
            //Log.d("OF J", Integer.toString(6 - j));
            if(skipFlag)
                continue;
            labels.add(dateFormatter.format(calendar.getTime()));
            entries.add(new Entry(6 - j, 0));
            count++;
        }
        Collections.reverse(entries);
        Collections.reverse(labels);

        LineDataSet dataset = new LineDataSet(entries, "Nutrition");
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
        ///
        isTriggered = true;
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
        LineDataSet exDataset = new LineDataSet(exEntries, "Exercise");
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
        boolean bSwitch = sp.getBoolean("switch_overlay_exercise_on_graph", false);
        tempSet = null;
        if(bSwitch) {
            Log.d("OF", "CALL!");
            tempSet = getSecondGraph();
            lines.add(tempSet);
            Collections.reverse(lines);
            //mGraphs.addDataSet(getSecondGraph());

            mGraphs = new LineData(lines);
            mLineChart.notifyDataSetChanged();
            mLineChart.setData(mGraphs);
            mLineChart.invalidate();
        } else if(!bSwitch && isTriggered) {
            Log.d("OF", "DISABLE CALL");
            //lines.remove(getSecondGraph());
            lines = new ArrayList<>();
            lines.add(getFirstGraph());
            Log.d("OFFFFB", Boolean.toString(getSecondGraph() == null));
            //mGraphs.addDataSet(getSecondGraph());
            mGraphs = new LineData(lines);
            mLineChart.notifyDataSetChanged();
            mLineChart.setData(mGraphs);
            mLineChart.invalidate();
        }
    }
}
