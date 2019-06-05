package com.example.shinj.navmain.InfoDevice;

import io.socket.client.Socket;

public interface InfoDevicePresenter {

    void takeDeviceInfo(Socket socket);
    void shutdownDevice(Socket socket);
    void rebootDevice(Socket socket);

    interface View {
        void showDeviceInfo(String osVersion, String freeMemory, String totalMemory, String infoCPU);
        void infoResErrorToast();
    }
}
