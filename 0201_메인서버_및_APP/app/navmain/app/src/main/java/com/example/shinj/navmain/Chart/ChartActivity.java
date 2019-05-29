package com.example.shinj.navmain.Chart;

import com.example.shinj.navmain.BaseActivity;
import com.example.shinj.navmain.IntentData;
import com.example.shinj.navmain.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.socket.client.Socket;


public class ChartActivity extends BaseActivity {
    static TextView mondayDate, mondayMaxTemper, mondayMinTemper, mondayMaxPH, mondayMinPH, mondayFeed,
            tuesdayDate, tuesdayMaxTemper, tuesdayMinTemper, tuesdayMaxPH, tuesdayMinPH, tuesdayFeed,
            wednesdayDate, wednesdayMaxTemper, wednesdayMinTemper, wednesdayMaxPH, wednesdayMinPH, wednesdayFeed,
            thursdayDate, thursdayMaxTemper, thursdayMinTemper, thursdayMaxPH, thursdayMinPH, thursdayFeed,
            fridayDate, fridayMaxTemper, fridayMinTemper, fridayMaxPH, fridayMinPH, fridayFeed,
            saturdayDate, saturdayMaxTemper, saturdayMinTemper, saturdayMaxPH, saturdayMinPH, saturdayFeed,
            sundayDate, sundayMaxTemper, sundayMinTemper, sundayMaxPH, sundayMinPH, sundayFeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 월
        mondayDate = findViewById(R.id.WeeklyChart_Monday_Date);
        mondayMaxTemper = findViewById(R.id.WeeklyChart_Monday_MaxTemper);
        mondayMinTemper = findViewById(R.id.WeeklyChart_Monday_MinTemper);
        mondayMaxPH = findViewById(R.id.WeeklyChart_Monday_MaxPH);
        mondayMinPH = findViewById(R.id.WeeklyChart_Monday_MinPH);
        mondayFeed = findViewById(R.id.WeeklyChart_Monday_Feed);
        // 화
        tuesdayDate = findViewById(R.id.WeeklyChart_Tuesday_Date);
        tuesdayMaxTemper = findViewById(R.id.WeeklyChart_Tuesday_MaxTemper);
        tuesdayMinTemper = findViewById(R.id.WeeklyChart_Tuesday_MinTemper);
        tuesdayMaxPH = findViewById(R.id.WeeklyChart_Tuesday_MaxPH);
        tuesdayMinPH = findViewById(R.id.WeeklyChart_Tuesday_MinPH);
        tuesdayFeed = findViewById(R.id.WeeklyChart_Tuesday_Feed);
        // 수
        wednesdayDate = findViewById(R.id.WeeklyChart_Wednesday_Date);
        wednesdayMaxTemper = findViewById(R.id.WeeklyChart_Wednesday_MaxTemper);
        wednesdayMinTemper = findViewById(R.id.WeeklyChart_Wednesday_MinTemper);
        wednesdayMaxPH = findViewById(R.id.WeeklyChart_Wednesday_MaxPH);
        wednesdayMinPH = findViewById(R.id.WeeklyChart_Wednesday_MinPH);
        wednesdayFeed = findViewById(R.id.WeeklyChart_Wednesday_Feed);
        // 목
        thursdayDate = findViewById(R.id.WeeklyChart_Thursday_Date);
        thursdayMaxTemper = findViewById(R.id.WeeklyChart_Thursday_MaxTemper);
        thursdayMinTemper = findViewById(R.id.WeeklyChart_Thursday_MinTemper);
        thursdayMaxPH = findViewById(R.id.WeeklyChart_Thursday_MaxPH);
        thursdayMinPH = findViewById(R.id.WeeklyChart_Thursday_MinPH);
        thursdayFeed = findViewById(R.id.WeeklyChart_Thursday_Feed);
        // 금
        fridayDate = findViewById(R.id.WeeklyChart_Friday_Date);
        fridayMaxTemper = findViewById(R.id.WeeklyChart_Friday_MaxTemper);
        fridayMinTemper = findViewById(R.id.WeeklyChart_Friday_MinTemper);
        fridayMaxPH = findViewById(R.id.WeeklyChart_Friday_MaxPH);
        fridayMinPH = findViewById(R.id.WeeklyChart_Friday_MinPH);
        fridayFeed = findViewById(R.id.WeeklyChart_Friday_Feed);
        // 토
        saturdayDate = findViewById(R.id.WeeklyChart_Saturday_Date);
        saturdayMaxTemper = findViewById(R.id.WeeklyChart_Saturday_MaxTemper);
        saturdayMinTemper = findViewById(R.id.WeeklyChart_Saturday_MinTemper);
        saturdayMaxPH = findViewById(R.id.WeeklyChart_Saturday_MaxPH);
        saturdayMinPH = findViewById(R.id.WeeklyChart_Saturday_MinPH);
        saturdayFeed = findViewById(R.id.WeeklyChart_Saturday_Feed);
        // 일
        sundayDate = findViewById(R.id.WeeklyChart_Sunday_Date);
        sundayMaxTemper = findViewById(R.id.WeeklyChart_Sunday_MaxTemper);
        sundayMinTemper = findViewById(R.id.WeeklyChart_Sunday_MinTemper);
        sundayMaxPH = findViewById(R.id.WeeklyChart_Sunday_MaxPH);
        sundayMinPH = findViewById(R.id.WeeklyChart_Sunday_MinPH);
        sundayFeed = findViewById(R.id.WeeklyChart_Sunday_Feed);

        drawDate();
        drawDailyChart();
        String today = DayOfWeek.getTodayDate();
        WeeklyChart weeklyChart = new WeeklyChart(today);
    }


    private void drawDailyChart() {

        LineChart lineChart;
        lineChart = (LineChart) findViewById(R.id.chart);
        MyMarkerView marker = new MyMarkerView(this, R.layout.markerview);
        marker.setChartView(lineChart);
        lineChart.setMarker(marker);

        ArrayList<Entry> temperatureValue = new ArrayList<>();
        temperatureValue.add(new Entry(0, 22));
        temperatureValue.add(new Entry(3, 23));
        temperatureValue.add(new Entry(6, 24));
        temperatureValue.add(new Entry(9, 23));
        temperatureValue.add(new Entry(12, 21));
        temperatureValue.add(new Entry(15, 22));
        temperatureValue.add(new Entry(18, 24));
        temperatureValue.add(new Entry(21, 25));
        temperatureValue.add(new Entry(24, 24));

        LineDataSet lineDataSet = new LineDataSet(temperatureValue, "온도");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleColorHole(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);
        xAxis.setLabelCount(9, true);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setLabelCount(10, true);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.getDescription().setEnabled(false);
        lineChart.invalidate();
    }

    public static void drawWeeklyChart(String dayOfWeek, String[] dailyValues) {
        Log.d("호출된 함수: ", new Object() {}.getClass().getEnclosingMethod().getName());
        switch (dayOfWeek){
            case "월":
                mondayMaxTemper.setText(dailyValues[0] + "°");
                mondayMinTemper.setText(dailyValues[1] + "°");
                mondayMaxPH.setText(dailyValues[2]);
                mondayMinPH.setText(dailyValues[3]);
                mondayFeed.setText(dailyValues[4] + "회");
                break;
            case "화":
                tuesdayMaxTemper.setText(dailyValues[0] + "°");
                tuesdayMinTemper.setText(dailyValues[1] + "°");
                tuesdayMaxPH.setText(dailyValues[2]);
                tuesdayMinPH.setText(dailyValues[3]);
                tuesdayFeed.setText(dailyValues[4] + "회");
                break;
            case "수":
                wednesdayMaxTemper.setText(dailyValues[0] + "°");
                wednesdayMinTemper.setText(dailyValues[1] + "°");
                wednesdayMaxPH.setText(dailyValues[2]);
                wednesdayMinPH.setText(dailyValues[3]);
                wednesdayFeed.setText(dailyValues[4] + "회");
                break;
            case "목":
                thursdayMaxTemper.setText(dailyValues[0] + "°");
                thursdayMinTemper.setText(dailyValues[1] + "°");
                thursdayMaxPH.setText(dailyValues[2]);
                thursdayMinPH.setText(dailyValues[3]);
                thursdayFeed.setText(dailyValues[4] + "회");
                break;
            case "금":
                fridayMaxTemper.setText(dailyValues[0] + "°");
                fridayMinTemper.setText(dailyValues[1] + "°");
                fridayMaxPH.setText(dailyValues[2]);
                fridayMinPH.setText(dailyValues[3]);
                fridayFeed.setText(dailyValues[4] + "회");
                break;
            case "토":
                saturdayMaxTemper.setText(dailyValues[0] + "°");
                saturdayMinTemper.setText(dailyValues[1] + "°");
                saturdayMaxPH.setText(dailyValues[2]);
                fridayMinPH.setText(dailyValues[3]);
                saturdayFeed.setText(dailyValues[4] + "회");
                break;
            case "일":
                sundayMaxTemper.setText(dailyValues[0] + "°");
                sundayMinTemper.setText(dailyValues[1] + "°");
                sundayMaxPH.setText(dailyValues[2]);
                sundayMinPH.setText(dailyValues[3]);
                sundayFeed.setText(dailyValues[4] + "회");
                break;
        }
    }

//    public void drawWeeklyChart() {
//
//        DayOfWeek dayOfWeek = new DayOfWeek();
//        String today = dayOfWeek.getTodayDate();
//        WeeklyChart weeklyChart = new WeeklyChart(today);
//        mondayMaxTemper.setText(weeklyChart.weeklyValue[0][0]);
//        mondayMinTemper.setText(weeklyChart.weeklyValue[0][1]);
//        mondayMaxPH.setText(weeklyChart.weeklyValue[0][2]);
//        mondayMinPH.setText(weeklyChart.weeklyValue[0][3]);
//
//
//        tuesdayMaxTemper.setText(weeklyChart.weeklyValue[1][0]);
//        tuesdayMinTemper.setText(weeklyChart.weeklyValue[1][1]);
//        tuesdayMaxPH.setText(weeklyChart.weeklyValue[1][2]);
//        tuesdayMinPH.setText(weeklyChart.weeklyValue[1][3]);
//
//        wednesdayMaxTemper.setText(weeklyChart.weeklyValue[2][0]);
//        wednesdayMinTemper.setText(weeklyChart.weeklyValue[2][1]);
//        wednesdayMaxPH.setText(weeklyChart.weeklyValue[2][2]);
//        wednesdayMinPH.setText(weeklyChart.weeklyValue[2][3]);
//
//
//        thursdayMaxTemper.setText(weeklyChart.weeklyValue[3][0]);
//        thursdayMinTemper.setText(weeklyChart.weeklyValue[3][1]);
//        thursdayMaxPH.setText(weeklyChart.weeklyValue[3][2]);
//        thursdayMinPH.setText(weeklyChart.weeklyValue[3][3]);
//
//        fridayMaxTemper.setText("xx");
//        fridayMinTemper.setText("xx");
//        fridayMaxPH.setText("xx");
//        fridayMinPH.setText("xx");
//
//        saturdayMaxTemper.setText("xx");
//        saturdayMinTemper.setText("xx");
//        saturdayMaxPH.setText("xx");
//        saturdayMinPH.setText("xx");
//
//        sundayMaxTemper.setText("xx");
//        sundayMinTemper.setText("xx");
//        sundayMaxPH.setText("xx");
//        sundayMinPH.setText("xx");
//    }

    public void drawDate() {
        DayOfWeek dayOfWeek = new DayOfWeek();
        String today = dayOfWeek.getTodayDate();
        String yoiL = dayOfWeek.getDayOfWeek(today);
        switch (yoiL) {
            case "월":
                mondayDate.setText(dayOfWeek.transformDateString(today));
                tuesdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 1));
                wednesdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 2));
                thursdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 3));
                fridayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 4));
                saturdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 5));
                sundayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 6));
                break;
            case "화":
                mondayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -1));
                tuesdayDate.setText(dayOfWeek.transformDateString(today));
                wednesdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 1));
                thursdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 2));
                fridayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 3));
                saturdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 4));
                sundayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 5));
                break;
            case "수":
                mondayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -2));
                tuesdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -1));
                wednesdayDate.setText(dayOfWeek.transformDateString(today));
                thursdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 1));
                fridayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 2));
                saturdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 3));
                sundayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 4));
                break;
            case "목":
                mondayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -3));
                tuesdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -2));
                wednesdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -1));
                thursdayDate.setText(dayOfWeek.transformDateString(today));
                fridayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 1));
                saturdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 2));
                sundayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 3));
                break;
            case "금":
                mondayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -4));
                tuesdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -3));
                wednesdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -2));
                thursdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -1));
                fridayDate.setText(dayOfWeek.transformDateString(today));
                saturdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 1));
                sundayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 2));
                break;
            case "토":
                mondayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -5));
                tuesdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -4));
                wednesdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -3));
                thursdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -2));
                fridayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -1));
                saturdayDate.setText(dayOfWeek.transformDateString(today));
                sundayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, 1));
                break;
            case "일":
                mondayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -6));
                tuesdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -5));
                wednesdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -4));
                thursdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -3));
                fridayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -2));
                saturdayDate.setText(dayOfWeek.setDateOnWeeklyChart(today, -1));
                sundayDate.setText(dayOfWeek.transformDateString(today));
                break;
        }

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_chart;
    }
}
