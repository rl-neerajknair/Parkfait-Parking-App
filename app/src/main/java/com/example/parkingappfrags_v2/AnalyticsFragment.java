package com.example.parkingappfrags_v2;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;


public class AnalyticsFragment extends Fragment {
    LineChart lineChart;
    LineDataSet lineDataSet = new LineDataSet(null, null);
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    LineData lineData;
    ArrayList<String> lineDatesValues = new ArrayList<>();
    private int fillColor = Color.argb(100,98,163,100);

    LinearLayout lineGraph_linearLayout;
    CardView lineGraph_cardView;

    LinearLayout barGraph_linearLayout;
    CardView barGraph_cardView;
    BarChart barChart;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> labelNames;
    ArrayList<DaySalesData> daySalesDataArrayList = new ArrayList<>();
    int monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    MyDatabaseHelper myDB = MyDatabaseHelper.getInstance(getContext());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);

        lineGraph_linearLayout = view.findViewById(R.id.lineGraph_linearLayout);
        lineGraph_cardView = view.findViewById(R.id.lineGraph_cardView);
        lineGraph_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandLineGraphCard();
                show_lineGraph();
            }
        });

        barGraph_linearLayout = view.findViewById(R.id.barGraph_linearLayout);
        barGraph_cardView = view.findViewById(R.id.barGraph_cardView);
        barGraph_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandBarGraphCard();
                show_barGraph();
            }
        });


        return view;
    }

    // Function to Animate and Expand the CardView containing 'Parking Information'
    private void expandLineGraphCard() {
        int v = (lineGraph_linearLayout.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

        TransitionManager.beginDelayedTransition(lineGraph_cardView , new AutoTransition());

        lineGraph_linearLayout.setVisibility(v);
    }

    private void expandBarGraphCard() {
        int v = (barGraph_linearLayout.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

        TransitionManager.beginDelayedTransition(barGraph_cardView , new AutoTransition());

        barGraph_linearLayout.setVisibility(v);
    }

    private void show_lineGraph(){


        lineChart = (LineChart) getActivity().findViewById(R.id.lineChart);

        lineDataSet.setLineWidth(3f);
        lineDataSet.setValues(getDataValues());
        lineDataSet.setLabel("DataSet 1");
        dataSets.clear();
        dataSets.add(lineDataSet);
        lineData = new LineData(dataSets);

        lineDataSet.setColor(fillColor);

        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setCircleColor(fillColor);
        lineDataSet.setCircleRadius(10f);
        lineDataSet.setCircleHoleRadius(6f);

        lineData.setDrawValues(true);

        lineChart.clear();
        lineChart.setData(lineData);
        lineChart.invalidate();

        lineChart.setDescription(null);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDrawGridBackground(false);

        //lineChart.getXAxis().setDrawGridLines(false);

        //lineChart.getAxisLeft().setLabelCount(10,false);
        //lineChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //lineChart.getAxisLeft().setDrawGridLines(false);

        lineChart.animateY(3000);
        lineChart.getXAxis().setEnabled(true);
        lineChart.getAxisLeft().setEnabled(true);
        lineChart.getAxisRight().setEnabled(false);
    }

    private void show_barGraph(){
        barChart = (BarChart) getActivity().findViewById(R.id.barChart);
        barEntryArrayList = new ArrayList<>();
        labelNames = new ArrayList<>();
        fillWeekSales();
        for (int i=0 ; i<daySalesDataArrayList.size(); i++){
            String day = daySalesDataArrayList.get(i).getDay();
            int sales = daySalesDataArrayList.get(i).getSales();
            barEntryArrayList.add(new BarEntry(i,sales));
            labelNames.add(day);
        }

        BarDataSet barDataSet
                = new BarDataSet(barEntryArrayList, "");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));

        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelNames.size());
        xAxis.setLabelRotationAngle(270);

        barChart.animateY(2000);
        barChart.invalidate();
    }

    private void fillWeekSales(){
        Cursor cursor = myDB.readAll_DAY_PRICE_Data_priceAccumulation();

        monday = 0;
        tuesday = 0;
        wednesday = 0;
        thursday = 0;
        friday = 0;
        saturday = 0;
        sunday = 0;

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            String day = String.valueOf(cursor.getString(0));

                if (day.toLowerCase().trim().equals("mon") || day.toLowerCase().trim().equals("monday"))
                    monday = monday + Integer.valueOf((int) cursor.getFloat(1));
                else if (day.toLowerCase().trim().equals("tue") || day.toLowerCase().trim().equals("tuesday"))
                    tuesday = tuesday + Integer.valueOf((int) cursor.getFloat(1));
                else if (day.toLowerCase().trim().equals("wed") || day.toLowerCase().trim().equals("wednesday"))
                    wednesday = wednesday + Integer.valueOf((int) cursor.getFloat(1));
                else if (day.toLowerCase().trim().equals("thu") || day.toLowerCase().trim().equals("thursday"))
                    thursday = thursday + Integer.valueOf((int) cursor.getFloat(1));
                else if (day.toLowerCase().trim().equals("fri") || day.toLowerCase().trim().equals("friday"))
                    friday = friday + Integer.valueOf((int) cursor.getFloat(1));
                else if (day.toLowerCase().trim().equals("sat") || day.toLowerCase().trim().equals("saturday"))
                    saturday = saturday + Integer.valueOf((int) cursor.getFloat(1));
                else if (day.toLowerCase().trim().equals("sun") || day.toLowerCase().trim().equals("sunday"))
                    sunday = sunday + Integer.valueOf((int) cursor.getFloat(1));
        }

        daySalesDataArrayList.clear();
        daySalesDataArrayList.add(new DaySalesData("Monday",monday));
        daySalesDataArrayList.add(new DaySalesData("Tuesday",tuesday));
        daySalesDataArrayList.add(new DaySalesData("Wednesday",wednesday));
        daySalesDataArrayList.add(new DaySalesData("Thursday",thursday));
        daySalesDataArrayList.add(new DaySalesData("Friday",friday));
        daySalesDataArrayList.add(new DaySalesData("Saturday",saturday));
        daySalesDataArrayList.add(new DaySalesData("Sunday",sunday));
    }

    private ArrayList<Entry> getDataValues() {
        ArrayList<Entry> dataVals = new ArrayList<>();
        Cursor cursor = myDB.readAll_DATE_PRICE_Data_priceAccumulation();

        for (int i=0 ; i<cursor.getCount() ; i++){
            cursor.moveToNext();
            dataVals.add(new Entry(cursor.getFloat(0) , cursor.getFloat(1)));
        }
        return dataVals;
    }

    private class MyValueFormatter implements IValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return null;
        }
    }
}