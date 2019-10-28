package com.example.shinj.navmain.UserSetting;

import android.util.Log;

import com.example.shinj.navmain.IntentData;

import io.socket.client.Socket;

public class ChangePasswordDialogImpl implements ChangePasswordDialogPresenter {

    Socket socket = IntentData.getInstance().getSocket();
    ChangePasswordDialogPresenter.View view;

    public ChangePasswordDialogImpl(ChangePasswordDialogPresenter.View view) {
        this.view = view;
    }

    @Override
    public void changePassword(String previewPw, String forwardPw) {

        socket.emit("changePassword", previewPw, forwardPw);

        socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
        }).on("changePasswordResult", (Object... objects) -> {
            Log.d("비밀번호 변경 결과", objects[0].toString());
            view.changePasswordResult(Integer.parseInt(objects[0].toString()));
        });
    }
}
