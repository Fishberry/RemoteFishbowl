package com.example.shinj.navmain.Water;

import io.socket.client.Socket;

public interface WaterFragPresenter {

    void proceedingCount(Socket socket);
    void reqChangeWater(Socket socket, int count);
    void pauseChangeWater(Socket socket);
    void saveReserveWater(Socket socket, WaterReserveFragment waterReserveFragment);

    interface View {
        void setCount(int count);
    }
}
