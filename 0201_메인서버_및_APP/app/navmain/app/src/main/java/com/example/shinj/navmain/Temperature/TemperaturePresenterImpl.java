package com.example.shinj.navmain.Temperature;

import android.util.Log;
import android.widget.EditText;

import io.socket.client.Socket;

public class TemperaturePresenterImpl implements TemperaturePresenter {
    TemperaturePresenter.View view;

    @Override
    public void saveTemperPH(Socket socket, EditText minTemper, EditText maxTemper, EditText minPH, EditText maxPH) {
        double minTemperValue = Double.parseDouble(minTemper.getText().toString());
        double maxTemperValue = Double.parseDouble(maxTemper.getText().toString());
        double minPHValue = Double.parseDouble(minPH.getText().toString());
        double maxPHValue = Double.parseDouble(maxPH.getText().toString());
        socket.emit("insertTemper", minTemperValue, maxTemperValue);
        socket.emit("insertPH", minPHValue, maxPHValue);
    }


    @Override
    public void initTemperPH(Socket socket) {
        socket.emit("insertTemper",-1, -1);
        socket.emit("insertPH", -1, -1);
    }
}
