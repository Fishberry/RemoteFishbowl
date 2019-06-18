package com.example.shinj.navmain.Chart;

import com.example.shinj.navmain.BaseActivity;
import com.example.shinj.navmain.IntentData;
import com.example.shinj.navmain.R;
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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

    private Socket socket;
    IntentData intentData;

    int dailyValueCount = 0;
    int pageCount = 0;
    int thisWeekCount = 0;

    String today = DayOfWeek.getTodayDate();
    static TextView mondayDate, mondayMaxTemper, mondayMinTemper, mondayMaxPH, mondayMinPH, mondayFeed,
            tuesdayDate, tuesdayMaxTemper, tuesdayMinTemper, tuesdayMaxPH, tuesdayMinPH, tuesdayFeed,
            wednesdayDate, wednesdayMaxTemper, wednesdayMinTemper, wednesdayMaxPH, wednesdayMinPH, wednesdayFeed,
            thursdayDate, thursdayMaxTemper, thursdayMinTemper, thursdayMaxPH, thursdayMinPH, thursdayFeed,
            fridayDate, fridayMaxTemper, fridayMinTemper, fridayMaxPH, fridayMinPH, fridayFeed,
            saturdayDate, saturdayMaxTemper, saturdayMinTemper, saturdayMaxPH, saturdayMinPH, saturdayFeed,
            sundayDate, sundayMaxTemper, sundayMinTemper, sundayMaxPH, sundayMinPH, sundayFeed;
    static ConstraintLayout weeklyChartLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentData = IntentData.getInstance();
        socket = intentData.getSocket();

        socket.emit("reqDailyValueCount", "앱에서 DailValue의 Count요청");
        socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
        }).on("resDailyValueCount", (Object... objects) -> {
            getDailyValueCount(objects[0].toString());
        });
        weeklyChartLayout = findViewById(R.id.weeklyChartLayout);
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

        drawDate(today);
        drawDailyChart();
        WeeklyChart weeklyChart = new WeeklyChart(today);
        thisWeekCount = WeeklyChart.getThisWeekCount();

        weeklyChartLayout.setOnTouchListener(new View.OnTouchListener() {
            float downXAxis, upXAxis, intervalXAxis;
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // 버튼을 눌렀을 때
                    downXAxis = event.getX();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // 버튼에서 손을 떼었을 때
                    upXAxis = event.getX();
                    intervalXAxis = downXAxis - upXAxis;
                    if( intervalXAxis < -350 && dailyValueCount > 7 ) { // 저번주 코드
                        Log.d("저번주코드:", "하하");
                        pageCount++;
                        String startDate = WeeklyChart.getStartDate(DayOfWeek.calculDate(today, -(7* pageCount) ));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drawDate(startDate);
                                WeeklyChart.getWeeklyData(startDate, DayOfWeek.calculDate(startDate, 6));
                            }
                        });
                        if(pageCount == 1) {
                            dailyValueCount -= thisWeekCount;
                            Log.d("저번주에:", ""+dailyValueCount +"dddd" +thisWeekCount);
                        } else {
                            dailyValueCount -= 7;
                        }
                    } else if(intervalXAxis > 350 && pageCount > 1) { // 다음주 코드
                        pageCount--;
                        String startDate = WeeklyChart.getStartDate(DayOfWeek.calculDate(today, -(7* pageCount) ));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drawDate(startDate);
                                WeeklyChart.getWeeklyData(startDate, DayOfWeek.calculDate(startDate, 6));
                            }
                        });
                        dailyValueCount += 7;
                    } else if(intervalXAxis > 350 && pageCount == 1) { // 이번주 전 페이지에서 이번주 페이지로 넘어올 때
                        pageCount--;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int nullDateCount = 0;
                                drawDate(today);
                                WeeklyChart.getWeeklyData(WeeklyChart.getStartDate(today), today);
                                if (thisWeekCount != 7) { // 이번주 데이터에서 아직 없는 요일(ex.오늘 목요일이면 금,토,일)은 WeeklyChart에 null로 표시
                                    while (7 - nullDateCount > thisWeekCount) {
                                        drawWeeklyChartINVISIBLE(DayOfWeek.getDayOfWeek(DayOfWeek.calculDate(today, ++nullDateCount)));
                                    }
                                }
                                dailyValueCount += thisWeekCount;
                            }
                        });
                    }
                }
                return true;
            }
        });
    }

    private void getDailyValueCount(String count){
        dailyValueCount = Integer.parseInt(count);
    }

    private void drawDailyChart() {

        LineChart lineChart;
        BarChart barChart;
        lineChart = (LineChart) findViewById(R.id.chart);
        barChart = (BarChart) findViewById(R.id.bar_chart);
        MyMarkerView marker = new MyMarkerView(this, R.layout.markerview);
        marker.setChartView(lineChart);
        lineChart.setMarker(marker);
        lineChart.bringToFront();       //해당 뷰를 위로 올리는 메소드.

        ArrayList<Entry> temperatureValue = new ArrayList<>();
        temperatureValue.add(new Entry(0, 22));
        temperatureValue.add(new Entry(3, 23));
        temperatureValue.add(new Entry(6, 24));
        temperatureValue.add(new Entry(9, 23));
        temperatureValue.add(new Entry(12, 21));
        temperatureValue.add(new Entry(15, 22));
        temperatureValue.add(new Entry(18, 24));
        temperatureValue.add(new Entry(21, 25));
        //temperatureValue.add(new Entry(24, 24));

//        ArrayList<Entry> phValue = new ArrayList<>();
//        phValue.add(new Entry(0, 8));
//        phValue.add(new Entry(3, 7));
//        phValue.add(new Entry(6, 8));
//        phValue.add(new Entry(9, 9));
//        phValue.add(new Entry(12, 7));
//        phValue.add(new Entry(15, 8));
//        phValue.add(new Entry(18, 7));
//        phValue.add(new Entry(21, 9));

        ArrayList<BarEntry> phValue = new ArrayList<>();
        phValue.add(new BarEntry(0, 8f));
        phValue.add(new BarEntry(3, 7f));
        phValue.add(new BarEntry(6, 8f));
        phValue.add(new BarEntry(9, 9f));
        phValue.add(new BarEntry(12, 7f));
        phValue.add(new BarEntry(15, 8f));
        phValue.add(new BarEntry(18, 7f));
        phValue.add(new BarEntry(21, 9f));

        //이것은 범주를 찍기 위해 추가한 요소입니다.
        //위에서 보시면 알다시피 데이터는 없지만, 범주를 추가하려면 Set은 만들어줘야되기 때문에, Set만 만들어주고 데이터는 집어넣지 않았습니다.
        //범주의 색깔은 빨간색으로
        LineDataSet lineDataSet = new LineDataSet(temperatureValue, "수질");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFFF0000"));
        lineDataSet.setCircleColorHole(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFFF0000"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        //실질적인 온도그래프의 설정.
        LineDataSet lineDataSet2 = new LineDataSet(temperatureValue, "온도");
        lineDataSet2.setLineWidth(2);
        lineDataSet2.setCircleRadius(6);
        lineDataSet2.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet2.setCircleColorHole(Color.BLUE);
        lineDataSet2.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet2.setDrawCircleHole(true);
        lineDataSet2.setDrawCircles(true);
        lineDataSet2.setDrawHorizontalHighlightIndicator(false);
        lineDataSet2.setDrawHighlightIndicators(false);
        lineDataSet2.setDrawValues(false);

//        LineDataSet lineDataSet2 = new LineDataSet(phValue, "수질");
//        lineDataSet2.setLineWidth(2);
//        lineDataSet2.setCircleRadius(6);
//        lineDataSet2.setCircleColor(Color.parseColor("#FF0000"));
//        lineDataSet2.setCircleColorHole(Color.RED);
//        lineDataSet2.setColor(Color.parseColor("#FF0000"));
//        lineDataSet2.setDrawCircleHole(true);
//        lineDataSet2.setDrawCircles(true);
//        lineDataSet2.setDrawHorizontalHighlightIndicator(false);
//        lineDataSet2.setDrawHighlightIndicators(false);
//        lineDataSet2.setDrawValues(false);

        //막대그래프 설정.
        BarDataSet barDataSet = new BarDataSet(phValue, "수질");
        barDataSet.setColors(Color.parseColor("#FF0000"));
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);

        //막대그래프 X축 수치 안 나오게 하기.
        XAxis barXAxis = barChart.getXAxis();
        barXAxis.setDrawLabels(false);
        barXAxis.setDrawAxisLine(false);
        barXAxis.setDrawGridLines(false);

        //막대그래프 Y축 왼쪽 안 나오게 하기.
        YAxis barYLAxis = barChart.getAxisLeft();
        barYLAxis.setDrawLabels(false);
        barYLAxis.setDrawAxisLine(false);
        barYLAxis.setDrawGridLines(false);

        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);    //설명 없애기 (이거 안 하면 오른쪽에 설명이 쓰여짐)
        barChart.getLegend().setEnabled(false);         //범주를 없애기 (이거 안 하면 겹쳐서 보이기에 없애고, 범주는 꺾은선그래프에 넣음)
        barChart.invalidate();                          //출력

//여기까지 바 그래프
//====================================================================================
//여기서부터 꺾은선그래프

        //꺾은선그래프 요소 Set들을 각 배열리스트에 집어넣어 꺾은선차트에 데이터 집어넣기.
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);
        dataSets.add(lineDataSet2);     //둘을 넣어줘야 범주도 함께 추가가 됩니다.
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);      //X 수치를 위 혹은 아래에 놓을 수 있습니다. 여기서 TOP, BOTTOM으로 변경만 하시면 되요.
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);
        xAxis.setLabelCount(9, true);
//        xAxis.setDrawLabels(false);
//        xAxis.setDrawAxisLine(false);
//        xAxis.setDrawGridLines(false);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setLabelCount(10, true);

        //Y축 오른쪽 수치 안 보이게 하기.
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
                saturdayMinPH.setText(dailyValues[3]);
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

    private void drawWeeklyChartINVISIBLE(String dayOfWeek){
        switch (dayOfWeek){
            case "월":
                mondayMaxTemper.setText("");
                mondayMinTemper.setText("");
                mondayMaxPH.setText("");
                mondayMinPH.setText("");
                mondayFeed.setText("");
                break;
            case "화":
                tuesdayMaxTemper.setText("");
                tuesdayMinTemper.setText("");
                tuesdayMaxPH.setText("");
                tuesdayMinPH.setText("");
                tuesdayFeed.setText("");
                break;
            case "수":
                wednesdayMaxTemper.setText("");
                wednesdayMinTemper.setText("");
                wednesdayMaxPH.setText("");
                wednesdayMinPH.setText("");
                wednesdayFeed.setText("");
                break;
            case "목":
                thursdayMaxTemper.setText("");
                thursdayMinTemper.setText("");
                thursdayMaxPH.setText("");
                thursdayMinPH.setText("");
                thursdayFeed.setText("");
                break;
            case "금":
                fridayMaxTemper.setText("");
                fridayMinTemper.setText("");
                fridayMaxPH.setText("");
                fridayMinPH.setText("");
                fridayFeed.setText("");
                break;
            case "토":
                saturdayMaxTemper.setText("");
                saturdayMinTemper.setText("");;
                saturdayMaxPH.setText("");
                saturdayMinPH.setText("");
                saturdayFeed.setText("");
                break;
            case "일":
                sundayMaxTemper.setText("");
                sundayMinTemper.setText("");
                sundayMaxPH.setText("");
                sundayMinPH.setText("");
                sundayFeed.setText("");
                break;
        }
    }

    public void drawDate(String targetDate) {
        String yoiL = DayOfWeek.getDayOfWeek(targetDate);
        switch (yoiL) {
            case "월":
                mondayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 0));
                tuesdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 1));
                wednesdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 2));
                thursdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 3));
                fridayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 4));
                saturdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 5));
                sundayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 6));
                break;
            case "화":
                mondayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -1));
                tuesdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 0));
                wednesdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 1));
                thursdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 2));
                fridayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 3));
                saturdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 4));
                sundayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 5));
                break;
            case "수":
                mondayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -2));
                tuesdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -1));
                wednesdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 0));
                thursdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 1));
                fridayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 2));
                saturdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 3));
                sundayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 4));
                break;
            case "목":
                mondayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -3));
                tuesdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -2));
                wednesdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -1));
                thursdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 0));
                fridayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 1));
                saturdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 2));
                sundayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 3));
                break;
            case "금":
                mondayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -4));
                tuesdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -3));
                wednesdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -2));
                thursdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -1));
                fridayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 0));
                saturdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 1));
                sundayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 2));
                break;
            case "토":
                mondayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -5));
                tuesdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -4));
                wednesdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -3));
                thursdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -2));
                fridayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -1));
                saturdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 0));
                sundayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 1));
                break;
            case "일":
                mondayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -6));
                tuesdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -5));
                wednesdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -4));
                thursdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -3));
                fridayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -2));
                saturdayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, -1));
                sundayDate.setText(DayOfWeek.setDateOnWeeklyChart(targetDate, 0));
                break;
        }

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_chart;
    }
}