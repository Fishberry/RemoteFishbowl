package com.example.shinj.navmain.Login;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.shinj.navmain.DB.DBElement;
import com.example.shinj.navmain.IntentData;

import io.socket.client.Socket;

public interface LoginPresenter {

    void rememberIP(DBElement dbElement, Socket socket, IntentData intentData, Context context);
    void connectServer(EditText ipEdit, EditText pwEdit, CheckBox checkBox, Context context);

    interface View {
        void errorLoginToast();
    }
}
