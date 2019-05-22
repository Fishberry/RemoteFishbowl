package com.example.shinj.navmain.Chart;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.shinj.navmain.BaseActivity;
import com.example.shinj.navmain.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ChartActivity extends BaseActivity {
    TextView mondayDate, mondayMaxTemper, mondayMinTemper, mondayMaxPH, mondayMinPH,
            tuesdayDate, tuesdayMaxTemper, tuesdayMinTemper, tuesdayMaxPH, tuesdayMinPH,
            wednesdayDate, wednesdayMaxTemper, wednesdayMinTemper, wednesdayMaxPH, wednesdayMinPH,
            thursdayDate, thursdayMaxTemper, thursdayMinTemper, thursdayMaxPH, thursdayMinPH,
            fridayDate, fridayMaxTemper, fridayMinTemper, fridayMaxPH, fridayMinPH,
            saturdayDate, saturdayMaxTemper, saturdayMinTemper, saturdayMaxPH, saturdayMinPH,
            sundayDate, sundayMaxTemper, sundayMinTemper, sundayMaxPH, sundayMinPH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 월
        mondayDate = findViewById(R.id.WeeklyChart_Monday_Date);
        mondayMaxTemper = findViewById(R.id.WeeklyChart_Monday_MaxTemper);
        mondayMinTemper = findViewById(R.id.WeeklyChart_Monday_MinTemper);
        mondayMaxPH = findViewById(R.id.WeeklyChart_Monday_MaxPH);
        mondayMinPH = findViewById(R.id.WeeklyChart_Monday_MinPH);
        // 화
        tuesdayDate = findViewById(R.id.WeeklyChart_Tuesday_Date);
        tuesdayMaxTemper = findViewById(R.id.WeeklyChart_Tuesday_MaxTemper);
        tuesdayMinTemper = findViewById(R.id.WeeklyChart_Tuesday_MinTemper);
        tuesdayMaxPH = findViewById(R.id.WeeklyChart_Tuesday_MaxPH);
        tuesdayMinPH = findViewById(R.id.WeeklyChart_Tuesday_MinPH);
        // 수
        wednesdayDate = findViewById(R.id.WeeklyChart_Wednesday_Date);
        wednesdayMaxTemper = findViewById(R.id.WeeklyChart_Wednesday_MaxTemper);
        wednesdayMinTemper = findViewById(R.id.WeeklyChart_Wednesday_MinTemper);
        wednesdayMaxPH = findViewById(R.id.WeeklyChart_Wednesday_MaxPH);
        wednesdayMinPH = findViewById(R.id.WeeklyChart_Wednesday_MinPH);
        // 목
        thursdayDate = findViewById(R.id.WeeklyChart_Thursday_Date);
        thursdayMaxTemper = findViewById(R.id.WeeklyChart_Thursday_MaxTemper);
        thursdayMinTemper = findViewById(R.id.WeeklyChart_Thursday_MinTemper);
        thursdayMaxPH = findViewById(R.id.WeeklyChart_Thursday_MaxPH);
        thursdayMinPH = findViewById(R.id.WeeklyChart_Thursday_MinPH);
        // 금
        fridayDate = findViewById(R.id.WeeklyChart_Friday_Date);
        fridayMaxTemper = findViewById(R.id.WeeklyChart_Friday_MaxTemper);
        fridayMinTemper = findViewById(R.id.WeeklyChart_Friday_MinTemper);
        fridayMaxPH = findViewById(R.id.WeeklyChart_Friday_MaxPH);
        fridayMinPH = findViewById(R.id.WeeklyChart_Friday_MinPH);
        // 토
        saturdayDate = findViewById(R.id.WeeklyChart_Saturday_Date);
        saturdayMaxTemper = findViewById(R.id.WeeklyChart_Saturday_MaxTemper);
        saturdayMinTemper = findViewById(R.id.WeeklyChart_Saturday_MinTemper);
        saturdayMaxPH = findViewById(R.id.WeeklyChart_Saturday_MaxPH);
        saturdayMinPH = findViewById(R.id.WeeklyChart_Saturday_MinPH);
        // 일
        sundayDate = findViewById(R.id.WeeklyChart_Sunday_Date);
        sundayMaxTemper = findViewById(R.id.WeeklyChart_Sunday_MaxTemper);
        sundayMinTemper = findViewById(R.id.WeeklyChart_Sunday_MinTemper);
        sundayMaxPH = findViewById(R.id.WeeklyChart_Sunday_MaxPH);
        sundayMinPH = findViewById(R.id.WeeklyChart_Sunday_MinPH);

        drawDate();
        drawDailyChart();
        drawWeeklyChart();
    }


    private void drawDailyChart() {

        LineChart lineChart;
        lineChart = (LineChart)findViewById(R.id.chart);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 1));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 4));
        entries.add(new Entry(5, 3));

        LineDataSet lineDataSet = new LineDataSet(entries, "속성명1");
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

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

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

    private void drawWeeklyChart() {

        mondayMaxTemper.setText("xx");
        mondayMinTemper.setText("xx");
        mondayMaxPH.setText("xx");
        mondayMinPH.setText("xx");


        tuesdayMaxTemper.setText("xx");
        tuesdayMinTemper.setText("xx");
        tuesdayMaxPH.setText("xx");
        tuesdayMinPH.setText("xx");

        wednesdayMaxTemper.setText("xx");
        wednesdayMinTemper.setText("xx");
        wednesdayMaxPH.setText("xx");
        wednesdayMinPH.setText("xx");


        thursdayMaxTemper.setText("xx");
        thursdayMinTemper.setText("xx");
        thursdayMaxPH.setText("xx");
        thursdayMinPH.setText("xx");

        fridayMaxTemper.setText("xx");
        fridayMinTemper.setText("xx");
        fridayMaxPH.setText("xx");
        fridayMinPH.setText("xx");

        saturdayMaxTemper.setText("xx");
        saturdayMinTemper.setText("xx");
        saturdayMaxPH.setText("xx");
        saturdayMinPH.setText("xx");

        sundayMaxTemper.setText("xx");
        sundayMinTemper.setText("xx");
        sundayMaxPH.setText("xx");
        sundayMinPH.setText("xx");
    }

    public String getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        String day = "";
        int nWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (nWeek) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;
        }
        return day;
    }

    public String getDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String str_date = df.format(new Date());    // 예시: str_date=2019-05-20
        return str_date;
    }

    public String transformDateString(String dateString) {
        String myMonth = dateString.substring(5,7);
        String myDay = dateString.substring(8,10);
        if( myMonth.charAt(0) == '0' ){
            myMonth = myMonth.substring(1);
        }
        if( myDay.charAt(0) == '0' ){
            myDay = myDay.substring(1);
        }
        String myFormattedDate = myMonth + "/" + myDay;

        return myFormattedDate;
    }

    private String calculDate(String date, int howManyDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            Date d = sdf.parse(date);
            c.setTime(d);
            c.add(Calendar.DATE, howManyDay);
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        String targetDate = sdf.format(c.getTime());
        return targetDate;
    }

    public void drawDate() {
        String dayOfWeek = getDayOfWeek();
        String today = getDateString();
        switch (dayOfWeek){
            case "월":
                mondayDate.setText(transformDateString(today));
                tuesdayDate.setText(transformDateString(calculDate(today, 1)));
                wednesdayDate.setText(transformDateString(calculDate(today, 2)));
                thursdayDate.setText(transformDateString(calculDate(today, 3)));
                fridayDate.setText(transformDateString(calculDate(today, 4)));
                saturdayDate.setText(transformDateString(calculDate(today, 5)));
                sundayDate.setText(transformDateString(calculDate(today, 6)));
                break;
            case "화":
                mondayDate.setText(transformDateString(calculDate(today, -1)));
                tuesdayDate.setText(transformDateString(today));
                wednesdayDate.setText(transformDateString(calculDate(today, 1)));
                thursdayDate.setText(transformDateString(calculDate(today, 2)));
                fridayDate.setText(transformDateString(calculDate(today, 3)));
                saturdayDate.setText(transformDateString(calculDate(today, 4)));
                sundayDate.setText(transformDateString(calculDate(today, 5)));
                break;
            case "수":
                mondayDate.setText(transformDateString(calculDate(today, -2)));
                tuesdayDate.setText(transformDateString(calculDate(today, -1)));
                wednesdayDate.setText(transformDateString(today));
                thursdayDate.setText(transformDateString(calculDate(today, 1)));
                fridayDate.setText(transformDateString(calculDate(today, 2)));
                saturdayDate.setText(transformDateString(calculDate(today, 3)));
                sundayDate.setText(transformDateString(calculDate(today, 4)));
                break;
            case "목":
                mondayDate.setText(transformDateString(calculDate(today, -3)));
                tuesdayDate.setText(transformDateString(calculDate(today, -2)));
                wednesdayDate.setText(transformDateString(calculDate(today, -1)));
                thursdayDate.setText(transformDateString(today));
                fridayDate.setText(transformDateString(calculDate(today, 1)));
                saturdayDate.setText(transformDateString(calculDate(today, 2)));
                sundayDate.setText(transformDateString(calculDate(today, 3)));
                break;
            case "금":
                mondayDate.setText(transformDateString(calculDate(today, -4)));
                tuesdayDate.setText(transformDateString(calculDate(today, -3)));
                wednesdayDate.setText(transformDateString(calculDate(today, -2)));
                thursdayDate.setText(transformDateString(calculDate(today, -1)));
                fridayDate.setText(transformDateString(today));
                saturdayDate.setText(transformDateString(calculDate(today, 1)));
                sundayDate.setText(transformDateString(calculDate(today, 2)));
                break;
            case "토":
                mondayDate.setText(transformDateString(calculDate(today, -5)));
                tuesdayDate.setText(transformDateString(calculDate(today, -4)));
                wednesdayDate.setText(transformDateString(calculDate(today, -3)));
                thursdayDate.setText(transformDateString(calculDate(today, -2)));
                fridayDate.setText(transformDateString(calculDate(today, -1)));
                saturdayDate.setText(transformDateString(today));
                sundayDate.setText(transformDateString(calculDate(today, 1)));
                break;
            case "일":
                mondayDate.setText(transformDateString(calculDate(today, -6)));
                tuesdayDate.setText(transformDateString(calculDate(today, -5)));
                wednesdayDate.setText(transformDateString(calculDate(today, -4)));
                thursdayDate.setText(transformDateString(calculDate(today, -3)));
                fridayDate.setText(transformDateString(calculDate(today, -2)));
                saturdayDate.setText(transformDateString(calculDate(today, -1)));
                sundayDate.setText(transformDateString(today));
                break;
        }

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_chart;
    }
}
