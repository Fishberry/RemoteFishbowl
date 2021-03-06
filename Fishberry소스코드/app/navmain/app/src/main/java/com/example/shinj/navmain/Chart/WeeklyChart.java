package com.example.shinj.navmain.Chart;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.shinj.navmain.IntentData;

import io.socket.client.Socket;

public class WeeklyChart {
    private static Socket socket;
    IntentData intentData;

    public WeeklyChart(String today) {
        intentData = IntentData.getInstance();
        socket = intentData.getSocket();
        getWeeklyData(getStartDate(today), today);
    }

    public static String getStartDate(String date) {
        int count = 0;
        String intervalDate = date;
        while (DayOfWeek.getDayOfWeek(intervalDate) != "월") {
            intervalDate = DayOfWeek.calculDate(date, --count);
        }
        return DayOfWeek.calculDate(date, count);
    }

    public static void getWeeklyData(String startDate, String endDate) {
        String targetDate;
        endDate = DayOfWeek.calculDate(endDate, 0);
        Log.d("호출된 함수: ", new Object() {
        }.getClass().getEnclosingMethod().getName());
        int count = 0;
        Log.d("시작날: ", startDate);
        Log.d("끝날: ", endDate);
        while (true) {
            targetDate = DayOfWeek.calculDate(startDate, count++);
            socket.emit("reqDailyValue", "\"" + targetDate + "\"");
            socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
            }).on("resDailyValue", (Object... objects) -> {
                String[] dailyValues = new String[5];
                dailyValues[0] = objects[0].toString();
                dailyValues[1] = objects[1].toString();
                dailyValues[2] = objects[2].toString();
                dailyValues[3] = objects[3].toString();
                dailyValues[4] = objects[4].toString();
                String yoiL = DayOfWeek.getDayOfWeek(objects[5].toString().replaceAll("\"", ""));
                Log.d("dailyValues", dailyValues[0] + " " + dailyValues[1] + " " + dailyValues[2] + " " + dailyValues[3] + " " + dailyValues[4]);
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ChartActivity.drawWeeklyChart(yoiL, dailyValues);
                    }
                }, 0);
            });
            if (targetDate.equals(endDate)) break;
//        if (!startDate.equals(endDate)) { // 논리오류 코드. 시작날이 월요일이면 월요일 if문이 없기 때문에 월요일 drawWeeklyChart를 안함.(by제우)
//            while (true) {
//                targetDate = DayOfWeek.calculDate(startDate, count++);
//                socket.emit("reqDailyValue", "\"" + targetDate + "\"");
//                socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
//                }).on("resDailyValue", (Object... objects) -> {
//                    String[] dailyValues = new String[5];
//                    dailyValues[0] = objects[0].toString();
//                    dailyValues[1] = objects[1].toString();
//                    dailyValues[2] = objects[2].toString();
//                    dailyValues[3] = objects[3].toString();
//                    dailyValues[4] = objects[4].toString();
//                    String yoiL = DayOfWeek.getDayOfWeek(objects[5].toString().replaceAll("\"", ""));
//                    Log.d("dailyValues", dailyValues[0]+ " " +dailyValues[1]+ " " +dailyValues[2] + " " +dailyValues[3] + " " +dailyValues[4]);
//                    Handler mHandler = new Handler(Looper.getMainLooper());
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            ChartActivity.drawWeeklyChart(yoiL, dailyValues);
//                        }
//                    }, 0);
//                });
//                if (targetDate.equals(endDate)) break;
//            }
        }
    }

    public static int getThisWeekCount() {
        String today = DayOfWeek.getTodayDate();
        int thisWeekCount = 0;
        if (DayOfWeek.getDayOfWeek(today) != "일") {
            while (DayOfWeek.getDayOfWeek(DayOfWeek.calculDate(today, thisWeekCount + 1)) != "월") {
                thisWeekCount++;
            }
            thisWeekCount = 7 - thisWeekCount;
        } else {
            thisWeekCount = 7;
        }
        return thisWeekCount;
    }
}
