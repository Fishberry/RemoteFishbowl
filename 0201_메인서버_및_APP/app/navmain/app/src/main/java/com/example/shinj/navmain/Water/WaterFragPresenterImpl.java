package com.example.shinj.navmain.Water;

import io.socket.client.Socket;

public class WaterFragPresenterImpl implements WaterFragPresenter {

    WaterFragPresenter.View view;

    @Override
    public void proceedingCount(Socket socket) {
        socket.emit("reqPercent", "reqPercent");
        socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
        }).on("resPercent", (Object... objects) -> {
            int count = Integer.parseInt(objects[0].toString());
            view.setCount(count);
        });
    }

    @Override
    public void pauseChangeWater(Socket socket) {
        socket.emit("reqWaterNowPause", "WaterPause");
    }

    @Override
    public void saveReserveWater(Socket socket, WaterReserveFragment waterReserveFragment) {
        String yearWater, monthWater, dayWater, hourWater, minunteWater;
        String timerWater;
        yearWater = Integer.toString(waterReserveFragment.datePickerWater.getYear());
        monthWater = Integer.toString(waterReserveFragment.datePickerWater.getMonth() + 1);
        dayWater = Integer.toString(waterReserveFragment.datePickerWater.getDayOfMonth());
        hourWater = Integer.toString(waterReserveFragment.timePickerWater.getHour());
        minunteWater = Integer.toString(waterReserveFragment.timePickerWater.getMinute());

        timerWater = "\"" + yearWater + "/" + monthWater + "/" + dayWater + "/" + hourWater + "/" + minunteWater + "/0\"";

        socket.emit("insertWater", timerWater);
    }
}
