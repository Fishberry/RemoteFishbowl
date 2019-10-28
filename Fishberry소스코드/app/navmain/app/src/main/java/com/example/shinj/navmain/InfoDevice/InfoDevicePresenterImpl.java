package com.example.shinj.navmain.InfoDevice;

import android.util.Log;

import io.socket.client.Socket;

public class InfoDevicePresenterImpl implements InfoDevicePresenter {

    InfoDevicePresenter.View view;

    public InfoDevicePresenterImpl(InfoDevicePresenter.View view) {
        this.view = view;
    }
    @Override
    public void takeDeviceInfo(Socket socket) {
        socket.emit("reqOS", "");
        socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
        }).on("resOS", (Object... objects) -> {
            try {
                String version = objects[0].toString();
                String freemem = String.valueOf((Integer.parseInt(objects[1].toString()) / 1048576)) + "MB";        //1048576은 1024 * 1024임. 바이트를 MB로 올리기 위한 행위임.
                String totalmem = String.valueOf((Integer.parseInt(objects[2].toString()) / 1048576)) + "MB";
                String cpuinfo = objects[3].toString() + " %";

                for (int i = 0; i < 4; i++)
                    Log.d("디바이스 정보 확인", objects[i].toString());

                view.showDeviceInfo(version, freemem, totalmem, cpuinfo);
            }
            catch (Exception e) {
                view.infoResErrorToast();
            }
        });
    }

    @Override
    public void shutdownDevice(Socket socket) {
        socket.emit("reqHalt", "");
    }

    @Override
    public void rebootDevice(Socket socket) {
        socket.emit("reqReboot", "");
    }
}
