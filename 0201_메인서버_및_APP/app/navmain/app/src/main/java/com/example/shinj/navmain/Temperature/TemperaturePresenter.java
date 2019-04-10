package com.example.shinj.navmain.Temperature;

import android.widget.EditText;

import io.socket.client.Socket;

public interface TemperaturePresenter {

    void saveTemperPH(Socket socket, EditText minTemper, EditText maxTemper, EditText minPH, EditText maxPH);
    void initTemperPH(Socket socket);

    interface View {

    }
}
