package com.example.shinj.navmain.Chart;

import com.example.shinj.navmain.BaseActivity;
import com.example.shinj.navmain.IntentData;
import com.example.shinj.navmain.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
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

    static View mondayView, tuesdayView, wednesdayView, thursdayView, fridayView, saturdayView, sundayView;

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

        mondayView = findViewById(R.id.monday_view);
        tuesdayView = findViewById(R.id.tuesday_view);
        wednesdayView = findViewById(R.id.wednesday_view);
        thursdayView = findViewById(R.id.thursday_view);
        fridayView = findViewById(R.id.friday_view);
        saturdayView = findViewById(R.id.saturday_view);
        sundayView = findViewById(R.id.sunday_view);

        //각 뷰를 위로 올려서 뷰가 먼저 닿도록 한다.
        mondayView.bringToFront();
        tuesdayView.bringToFront();
        wednesdayView.bringToFront();
        thursdayView.bringToFront();
        fridayView.bringToFront();
        saturdayView.bringToFront();
        sundayView.bringToFront();

        //뷰의 클릭 리스너
        mondayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChartActivity.this, "월요일", Toast.LENGTH_SHORT).show();
            }
        });

        tuesdayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChartActivity.this, "화요일", Toast.LENGTH_SHORT).show();
            }
        });

        wednesdayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChartActivity.this, "수요일", Toast.LENGTH_SHORT).show();
            }
        });

        thursdayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChartActivity.this, "목요일", Toast.LENGTH_SHORT).show();
            }
        });

        fridayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChartActivity.this, "금요일", Toast.LENGTH_SHORT).show();
            }
        });

        saturdayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChartActivity.this, "토요일", Toast.LENGTH_SHORT).show();
            }
        });

        sundayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChartActivity.this, "일요일", Toast.LENGTH_SHORT).show();
            }
        });

        //뷰의 터치 리스너
        //여기서 코드가 많이 드러워지는 이유
        //기본적으로 안드로이드에서 뷰는 겹치게 되면 최상위의 뷰만 건드려지게 됩니다.
        //그렇기에 터치를 표현할 뷰가 위로 올라오게 되면 반대로 weeklyChartLayout을 못 건드리게 된다는 겁니다. (아래에 있으니까)
        //반대로 레이아웃을 위로 올려버리면 뷰가 아래로 내려와서 뷰를 못 건드리게 되고, 그 때문에 클릭을 인식하지 못하게 됩니다.
        //그렇기 때문에 레이아웃의 코드를 뷰로 올려서 각 뷰마다 적용시켰습니다.

        mondayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float downXAxis = 0;
                float upXAxis = 0;
                float intervalXAxis = 0;

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    downXAxis = motionEvent.getX();
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upXAxis = motionEvent.getX();
                    intervalXAxis = downXAxis - upXAxis;
                    Log.d("월요일", String.valueOf(intervalXAxis));

                    //레이아웃 코드르 여기로 가져왔습니다.
                    isTurnWeeklyChart(intervalXAxis);
                }

                //터치를 했다면
                if (intervalXAxis > -350 && intervalXAxis < 350)
                    //return true로 해버리면 touch에서 그만둔다. 하지만, longclick과 click 리스너는 전부 touch에서 파생되며 false로 해야 다음으로 넘어가집니다.
                    //실제로 touch와 longclick과 click을 두고 log를 띄워본다면 touch -> click -> longclick순으로 메소드가 진행됩니다.
                    //이하의 것들도 전부 똑같으니 생략하도록 합니다.
                    return false;

                return true;
            }
        });

        tuesdayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float downXAxis = 0;
                float upXAxis = 0;
                float intervalXAxis = 0;

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    downXAxis = motionEvent.getX();
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upXAxis = motionEvent.getX();
                    intervalXAxis = downXAxis - upXAxis;
                    Log.d("화요일", String.valueOf(intervalXAxis));

                    isTurnWeeklyChart(intervalXAxis);
                }

                if (intervalXAxis > -350 && intervalXAxis < 350)
                    return false;

                return true;
            }
        });

        wednesdayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float downXAxis = 0;
                float upXAxis = 0;
                float intervalXAxis = 0;

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    downXAxis = motionEvent.getX();
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upXAxis = motionEvent.getX();
                    intervalXAxis = downXAxis - upXAxis;
                    Log.d("수요일", String.valueOf(intervalXAxis));

                    isTurnWeeklyChart(intervalXAxis);
                }

                if (intervalXAxis > -350 && intervalXAxis < 350)
                    return false;

                return true;
            }
        });

        thursdayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float downXAxis = 0;
                float upXAxis = 0;
                float intervalXAxis = 0;

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    downXAxis = motionEvent.getX();
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upXAxis = motionEvent.getX();
                    intervalXAxis = downXAxis - upXAxis;
                    Log.d("목요일", String.valueOf(intervalXAxis));

                    isTurnWeeklyChart(intervalXAxis);
                }

                if (intervalXAxis > -350 && intervalXAxis < 350)
                    return false;

                return true;
            }
        });

        fridayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float downXAxis = 0;
                float upXAxis = 0;
                float intervalXAxis = 0;

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    downXAxis = motionEvent.getX();
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upXAxis = motionEvent.getX();
                    intervalXAxis = downXAxis - upXAxis;
                    Log.d("금요일", String.valueOf(intervalXAxis));

                    isTurnWeeklyChart(intervalXAxis);
                }

                if (intervalXAxis > -350 && intervalXAxis < 350)
                    return false;

                return true;
            }
        });

        saturdayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float downXAxis = 0;
                float upXAxis = 0;
                float intervalXAxis = 0;

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    downXAxis = motionEvent.getX();
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upXAxis = motionEvent.getX();
                    intervalXAxis = downXAxis - upXAxis;
                    Log.d("토요일", String.valueOf(intervalXAxis));

                    isTurnWeeklyChart(intervalXAxis);
                }

                if (intervalXAxis > -350 && intervalXAxis < 350)
                    return false;

                return true;
            }
        });

        sundayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float downXAxis = 0;
                float upXAxis = 0;
                float intervalXAxis = 0;

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    downXAxis = motionEvent.getX();
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upXAxis = motionEvent.getX();
                    intervalXAxis = downXAxis - upXAxis;
                    Log.d("일요일", String.valueOf(intervalXAxis));

                    isTurnWeeklyChart(intervalXAxis);
                }

                if (intervalXAxis > -350 && intervalXAxis < 350)
                    return false;

                return true;
            }
        });

        //해당 레이아웃은 뷰에 가려져 더 이상 건드릴 수 없으므로 주석처리 했습니다. 만약, 뷰를 없애게 된다면 다시 이 메소드를 이용해주세요.
/*
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
*/
    }

    private void getDailyValueCount(String count){
        dailyValueCount = Integer.parseInt(count);
    }

    private void drawDailyChart() {

        LineChart lineChartTemper, lineChartPH;
        lineChartTemper = (LineChart) findViewById(R.id.graph_temperature);
        lineChartPH = (LineChart)findViewById(R.id.graph_ph);
        MyMarkerView marker = new MyMarkerView(this, R.layout.markerview);
        marker.setChartView(lineChartTemper);
        lineChartTemper.setMarker(marker);

        // 온도 데이터
        ArrayList<Entry> temperatureValue = new ArrayList<>();
        temperatureValue.add(new Entry(0, 22f));
        temperatureValue.add(new Entry(3, 23f));
        temperatureValue.add(new Entry(6, 24f));
        temperatureValue.add(new Entry(9, 23f));
        temperatureValue.add(new Entry(12, 21f));
        temperatureValue.add(new Entry(15, 22f));
        temperatureValue.add(new Entry(18, 24f));
        temperatureValue.add(new Entry(21, 25f));
        temperatureValue.add(new Entry(24, 24f));

        // pH 데이터
        ArrayList<Entry> phValue = new ArrayList<>();
        phValue.add(new Entry(0, 8f));
        phValue.add(new Entry(3, 7f));
        phValue.add(new Entry(6, 7.5f));
        phValue.add(new Entry(9, 7.35f));
        phValue.add(new Entry(12, 7.65f));
        phValue.add(new Entry(15, 7.9f));
        phValue.add(new Entry(18, 8.3f));
        phValue.add(new Entry(21, 7.85f));
        phValue.add(new Entry(24, 7.61f));

        // 온도그래프부분
        LineDataSet lineDataSetTemper = new LineDataSet(temperatureValue, "온도");
        lineDataSetTemper.setLineWidth(2);
        lineDataSetTemper.setCircleRadius(4);
        lineDataSetTemper.setCircleColor(Color.parseColor("#0431B4"));
        lineDataSetTemper.setCircleColorHole(Color.BLUE);
        lineDataSetTemper.setColor(Color.parseColor("#0431B4"));
        lineDataSetTemper.setDrawCircleHole(true);
        lineDataSetTemper.setDrawCircles(true);
        lineDataSetTemper.setDrawHorizontalHighlightIndicator(false);
        lineDataSetTemper.setDrawHighlightIndicators(false);
        lineDataSetTemper.setDrawValues(false);

        LineData lineDataTemper = new LineData(lineDataSetTemper);
        lineChartTemper.setData(lineDataTemper);

        XAxis xAxisTemper = lineChartTemper.getXAxis();
        xAxisTemper.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisTemper.setTextColor(Color.WHITE);
        xAxisTemper.enableGridDashedLine(8, 24, 0);
        xAxisTemper.setLabelCount(9, true);

        YAxis yLAxisTemper = lineChartTemper.getAxisLeft();
        yLAxisTemper.setTextColor(Color.BLACK);
        yLAxisTemper.setLabelCount(10, true);

        YAxis yRAxisTemper = lineChartTemper.getAxisRight();
        yRAxisTemper.setDrawLabels(false);
        yRAxisTemper.setDrawAxisLine(false);
        yRAxisTemper.setDrawGridLines(false);

        lineChartTemper.getLegend().setEnabled(false);
        lineChartTemper.setDoubleTapToZoomEnabled(false);
        lineChartTemper.setDrawGridBackground(false);
        lineChartTemper.animateY(1000, Easing.EasingOption.EaseInCubic);
        lineChartTemper.getDescription().setEnabled(false);
        lineChartTemper.invalidate();

        // pH 그래프 부분
        LineDataSet lineDataSetPH = new LineDataSet(phValue, "pH");
        lineDataSetPH.setLineWidth(2);
        lineDataSetPH.setCircleRadius(4);
        lineDataSetPH.setCircleColor(Color.parseColor("#FFFF0000"));
        lineDataSetPH.setCircleColorHole(Color.BLUE);
        lineDataSetPH.setColor(Color.parseColor("#FFFF0000"));
        lineDataSetPH.setDrawCircleHole(true);
        lineDataSetPH.setDrawCircles(true);
        lineDataSetPH.setDrawHorizontalHighlightIndicator(false);
        lineDataSetPH.setDrawHighlightIndicators(false);
        lineDataSetPH.setDrawValues(false);

        LineData lineDataPH = new LineData(lineDataSetPH);
        lineChartPH.setData(lineDataPH);

        XAxis xAxisPH = lineChartPH.getXAxis();
        xAxisPH.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisPH.setTextColor(Color.BLACK);
        xAxisPH.enableGridDashedLine(8, 24, 0);
        xAxisPH.setLabelCount(9, true);

        YAxis yLAxisPH = lineChartPH.getAxisLeft();
        yLAxisPH.setTextColor(Color.BLACK);
        yLAxisPH.setDrawLabels(false);
        yLAxisPH.setDrawAxisLine(false);
        yLAxisPH.setDrawGridLines(false);

        YAxis yRAxisPH = lineChartPH.getAxisRight();
        yRAxisPH.setLabelCount(10, true);

        lineChartPH.getLegend().setEnabled(false);
        lineChartPH.setDoubleTapToZoomEnabled(false);
        lineChartPH.setDrawGridBackground(false);
        lineChartPH.animateY(1000, Easing.EasingOption.EaseInCubic);
        lineChartPH.getDescription().setEnabled(false);
        lineChartPH.invalidate();
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

    public void isTurnWeeklyChart(float intervalXAxis) {
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

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_chart;
    }
}