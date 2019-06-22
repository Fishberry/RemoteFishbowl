package com.example.shinj.navmain.Chart;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.shinj.navmain.IntentData;

import io.socket.client.Socket;

public class DailyChart {

    private static Socket socket;
    IntentData intentData;

    public DailyChart(String today) {
        intentData = IntentData.getInstance();
        socket = intentData.getSocket();
        getDailyData(today, "오늘");
    }

    public static void getDailyData(String targetDate, String targetDayOfWeek) {
        Log.d("호출된 함수: ", new Object() {
        }.getClass().getEnclosingMethod().getName());
        socket.emit("reqDaily3HourValue", targetDate);
        socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
        }).on("resDaily3HourValue", (Object... objects) -> {
            String[] daily3hourValues = objects[0].toString().split(",");
            String[] userSettingTemper = new String[2];
            String[] userSettingPH = new String[2];
            daily3hourValues[0] = daily3hourValues[0].substring(1);
            daily3hourValues[daily3hourValues.length-1] = daily3hourValues[daily3hourValues.length-1].substring(0, daily3hourValues[daily3hourValues.length-1].length()-1);
            for(int i=0; i<2; i++){
                userSettingTemper[i] = objects[i+1].toString();
                userSettingPH[i] = objects[i+3].toString();
            }
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChartActivity.drawDailyChart(targetDate, targetDayOfWeek, daily3hourValues, userSettingTemper, userSettingPH);
                }
            }, 0);
        });
    }
}
