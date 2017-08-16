package com.app.dmitryteplyakov.sportdiary.Overview;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.dmitryteplyakov.sportdiary.Core.Day.Day;
import com.app.dmitryteplyakov.sportdiary.Core.Day.DayStorage;
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

        //if(NutritionStorage.get(getActivity()).getNutritions().size() >= 7) {
        if(true) {
            ArrayList<Entry> entries = new ArrayList<>();
            labels = new ArrayList<>();
            List<NutritionDay> nutritionDayList = NutritionDayStorage.get(getActivity()).getNutritionDays();
            Collections.reverse(nutritionDayList);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM");
            int count = 0;
            boolean skipFlag;
            //for (NutritionDay nDay : nutritionDayList) {
            for(int j = 0; j < 7; j++) {
                Log.d("OF size", Integer.toString(entries.size()));
                skipFlag = false;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                int day = calendar.get(Calendar.DAY_OF_MONTH) - j;
                calendar.set(Calendar.DAY_OF_MONTH, day);
                Calendar date = Calendar.getInstance();
                for (NutritionDay nDay : nutritionDayList) {
                    date.setTime(nDay.getDate());
                    Log.d("OF compare", Integer.toString(day) + " " + dateFormatter.format(date.getTime()));
                    if(date.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                        labels.add(dateFormatter.format(nDay.getDate()));
                        Log.d("OF", dateFormatter.format(nDay.getDate()));
                        int resultEnergy = 0;
                        for (Nutrition nutrition : NutritionStorage.get(getActivity()).getNutritionsByParentDayId(nDay.getId())) {
                            resultEnergy += nutrition.getResultEnergy();
                        }
                        entries.add(new Entry(6 - j, resultEnergy));
                        skipFlag = true;
                        count++;
                    }
                }
                Log.d("OF J", Integer.toString(6 - j));
                if(skipFlag)
                    continue;
                labels.add(dateFormatter.format(calendar.getTime()));
                entries.add(new Entry(6 - j, 0));
                Log.d("OF", dateFormatter.format(calendar.getTime()));
                count++;
            }
            Collections.reverse(entries);
            Collections.reverse(labels);
            /*for(int i = 0; i < nutritionDayList.size(); i++) {
                NutritionDay nDay;
                if(nutritionDayList.size() - 7 < 0) {
                    entries.add(new Entry(count, 0));
                } else {
                    nDay = nutritionDayList.get(i);
                    if (new Date().getTime() - nDay.getDate().getTime() <= (1000 * 60 * 60 * 24 * 7)) {
                        labels.set(i, dateFormatter.format(nDay.getDate()));
                        Log.d("OF", "size labels: " + Integer.toString(labels.size()));
                        int resultEnergy = 0;
                        for (Nutrition nutrition : NutritionStorage.get(getActivity()).getNutritionsByParentDayId(nDay.getId())) {
                            resultEnergy += nutrition.getResultEnergy();
                        }
                        entries.add(new Entry(count, resultEnergy));
                        Log.d("OF", "size entries: " + Integer.toString(entries.size()));
                    }
                }
                count++;
            }*/
            LineDataSet dataset = new LineDataSet(entries, "");
            dataset.setFillColor(Color.GREEN);
            dataset.setColor(Color.GREEN);
            dataset.setFillAlpha(90);
            dataset.setHighlightEnabled(false);
            dataset.setValueTextSize(10f);
            dataset.setDrawFilled(true);
            mLineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axisBase) {
                    Log.d("OF FL", Float.toString(value));
                    if(value < 0)
                            return "";
                    else if(value == 0) {
                        Log.d("OF", labels.get(0) + Integer.toString((int) labels.size()));
                        return labels.get(0);
                    } else {
                        Log.d("OF", labels.get(((int) (value))) + Integer.toString((int) labels.size()));
                        return labels.get(((int) (value)));
                    }
                }
            });
            dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            LineData data = new LineData(dataset);
            mLineChart.setData(data);
            mLineChart.animateY(600);
            Description description = new Description();
            description.setText(getString(R.string.fragment_program_energy_hint));
            mLineChart.setDescription(description);
        }
        return v;
    }
}
