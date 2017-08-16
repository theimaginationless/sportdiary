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
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Dmitry on 22.07.2017.
 */

public class OverviewFragment extends Fragment {
    private LineChart mLineChart;

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
        mLineChart.getXAxis().setGranularity(1);
        mLineChart.getXAxis().setGranularityEnabled(true);

        ArrayList<Entry> entries = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();
        List<NutritionDay> nutritionDayList = NutritionDayStorage.get(getActivity()).getNutritionDays();
        Collections.reverse(nutritionDayList);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM");
        int count = 0;
        for (NutritionDay nDay : nutritionDayList) {
            if(new Date().getTime() - nDay.getDate().getTime() <= (1000 * 60 * 60 * 24 * 7)) {
                labels.add(dateFormatter.format(nDay.getDate()));
                int resultEnergy = 0;
                for(Nutrition nutrition : NutritionStorage.get(getActivity()).getNutritionsByParentDayId(nDay.getId())) {
                    resultEnergy += nutrition.getResultEnergy();
                }
                entries.add(new Entry(count, resultEnergy));
                Log.d("OF", "size: " + Integer.toString(entries.size()));
                count++;
            }
        }
        LineDataSet dataset = new LineDataSet(entries, "");
        dataset.setFillColor(Color.GREEN);
        dataset.setColor(Color.GREEN);
        dataset.setFillAlpha(90);
        dataset.setHighlightEnabled(false);
        dataset.setValueTextSize(10f);
        dataset.setDrawFilled(true);
        //dataset.setDrawCubic(true);
        mLineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axisBase) {
                Log.d("OF", Integer.toString(((int) (value))));
                return labels.get(((int) (value)));
            }
        });
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        LineData data = new LineData(dataset);
        mLineChart.setData(data);
        mLineChart.animateY(600);
        mLineChart.setDescription(new Description());
        return v;
    }
}
