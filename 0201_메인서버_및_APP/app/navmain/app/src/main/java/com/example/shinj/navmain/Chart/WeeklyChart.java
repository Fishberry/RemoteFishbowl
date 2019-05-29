package com.example.shinj.navmain.Chart;

import android.util.Log;
import com.example.shinj.navmain.IntentData;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.socket.client.Socket;

public class WeeklyChart {
    private Socket socket;
    IntentData intentData;
    DayOfWeek dayOfWeek;
    String today;

//    public String[][] weeklyValue; // 1차원은 월,화,수... 2차원은 maxTemp, minTemp...

    public WeeklyChart(String today) {
//        weeklyValue = new String[7][5];
        intentData = IntentData.getInstance();
        socket = intentData.getSocket();
        this.today = today;
//        getWeeklyData(getStartDate(today), today);
        getWeeklyData("2019/5/20", "2019/5/24");
    }
    public String getStartDate(String date) {
        int count = 0;
        while( !DayOfWeek.getDayOfWeek(date).equals("월") ) {
            DayOfWeek.calculDate(date, -1);
            count--;
        }
        return  DayOfWeek.calculDate(date, count);
    }

//    public void requestDailyValues(String date) {
//        Log.d("호출된 함수: ", new Object() {}.getClass().getEnclosingMethod().getName());
//        socket.emit("reqDailyValue", "\"" + date + "\"");
//        socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
//        }).on("resDailyValue", (Object... objects) -> {
//            String[] dailyValues = new String[5];
//            dailyValues[0] = objects[0].toString();
//            dailyValues[1] = objects[1].toString();
//            dailyValues[2] = objects[2].toString();
//            dailyValues[3] = objects[3].toString();
//            dailyValues[4] = objects[4].toString();
//            String dayOfweek = DayOfWeek.getDayOfWeek(date);
//            switch (dayOfweek){
//                case "월":
//                    ChartActivity.mondayMaxTemper.setText(dailyValues[0]);
//                    ChartActivity.mondayMinTemper.setText(dailyValues[1]);
//                    ChartActivity.mondayMaxPH.setText(dailyValues[2]);
//                    ChartActivity.mondayMinPH.setText(dailyValues[3]);
//                    break;
//                case "화":
//                    ChartActivity.tuesdayMaxTemper.setText(dailyValues[0]);
//                    ChartActivity.tuesdayMinTemper.setText(dailyValues[1]);
//                    ChartActivity.tuesdayMaxPH.setText(dailyValues[2]);
//                    ChartActivity.tuesdayMinPH.setText(dailyValues[3]);
//                    break;
//                case "수":
//                    ChartActivity.wednesdayMaxTemper.setText(dailyValues[0]);
//                    ChartActivity.wednesdayMinTemper.setText(dailyValues[1]);
//                    ChartActivity.wednesdayMaxPH.setText(dailyValues[2]);
//                    ChartActivity.wednesdayMinPH.setText(dailyValues[3]);
//                    break;
//                case "목":
//                    ChartActivity.thursdayMaxTemper.setText(dailyValues[0]);
//                    ChartActivity.thursdayMinTemper.setText(dailyValues[1]);
//                    ChartActivity.thursdayMaxPH.setText(dailyValues[2]);
//                    ChartActivity.thursdayMinPH.setText(dailyValues[3]);
//                    break;
//                case "금":
//                    ChartActivity.fridayMaxTemper.setText("xx");
//                    ChartActivity.fridayMinTemper.setText("xx");
//                    ChartActivity.fridayMaxPH.setText("xx");
//                    ChartActivity.fridayMinPH.setText("xx");
//                    break;
//                case "토":
//                    ChartActivity.saturdayMaxTemper.setText("xx");
//                    ChartActivity.saturdayMinTemper.setText("xx");
//                    ChartActivity.saturdayMaxPH.setText("xx");
//                    ChartActivity.saturdayMinPH.setText("xx");
//                    break;
//                case "일":
//                    ChartActivity.sundayMaxTemper.setText("xx");
//                    ChartActivity.sundayMinTemper.setText("xx");
//                    ChartActivity.sundayMaxPH.setText("xx");
//                    ChartActivity.sundayMinPH.setText("xx");
//                    break;
//            }
//        });
//    }

    public void getWeeklyData(String startDate, String endDate) {
        String targetDate = startDate;
        Log.d("호출된 함수: ", new Object() {}.getClass().getEnclosingMethod().getName());
        int count = 0;
        if( !startDate.equals(endDate) ) {
            while (true) {
                targetDate = dayOfWeek.calculDate(startDate, count++);
                System.out.println("targetDate: " + targetDate);
                socket.emit("reqDailyValue", "\"" + targetDate + "\"");
                socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                }).on("resDailyValue", (Object... objects) -> {
                    String[] dailyValues = new String[5];
                    dailyValues[0] = objects[0].toString();
                    dailyValues[1] = objects[1].toString();
                    dailyValues[2] = objects[2].toString();
                    dailyValues[3] = objects[3].toString();
                    dailyValues[4] = objects[4].toString();
                    System.out.println("objects[5]: " + objects[5].toString());
                    String yoiL = DayOfWeek.getDayOfWeek(objects[5].toString().replaceAll("\"", ""));
                    System.out.println("요일: " + yoiL);
                    ChartActivity.drawWeeklyChart(yoiL, dailyValues);
                });
                if( targetDate.equals(endDate) ) break;
                }
        }

//        if( !startDate.equals(endDate) ) {
//            while (true) {
//                targetDate = dayOfWeek.calculDate(startDate, count++);
//                requestDailyValues(targetDate);
//                if( targetDate.equals(endDate) ) break;
//            }
//        }
    }
//        Log.d("호출된 함수: ", new Object() {
//        }.getClass().getEnclosingMethod().getName());
////        int count = 1;
//        String targetDate = startDate;
//        String[] tempArr;
//        for (int i = 0; i < 4; i++) {
//            tempArr = requestDailyValues(targetDate);
//            targetDate = dayOfWeek.calculDate(targetDate, 1);
//            System.out.println("targetDate: " + targetDate);
//            for (int j = 0; j < 5; j++) {
//                weeklyData[i][j] = tempArr[j];
//            }
//        }
//        return weeklyData;
//    }
//        String tempDate;
//        if( !startDate.equals(endDate) ) {
//            while (true) {
//                tempDate = dayOfWeek.calculDate(startDate, count);
//                if( tempDate.equals(endDate) ) break;
//                count++;
//            }
//        }

//        targetDate = dayOfWeek.calculDate(targetDate, 1);
//        socket.emit("reqDailyValue", "\"" + targetDate + "\"");
//        socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
//        }).on("resDailyValue", (Object... objects) -> {
//            this.weeklyData[0][0] = objects[0].toString();
//            this.weeklyData[0][1] = objects[1].toString();
//        });
//        this.weeklyData[0][2] = "2";
//        this.weeklyData[0][3] = "3";
//        this.weeklyData[0][4] = "4";
//    }
//    }
}

