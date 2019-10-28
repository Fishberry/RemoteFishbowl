package com.example.shinj.navmain.UserSetting;

import android.content.Context;

import com.example.shinj.navmain.DB.DBHelper;
import com.example.shinj.navmain.IntentData;

import io.socket.client.Socket;

public class SettingPresenterImpl implements SettingPresenter {

    SettingPresenter.View view;
    Context context = null;

    public SettingPresenterImpl(Context context) {
        this.context = context;
    }

    @Override
    public void logoutDBUpdate(DBHelper dbHelper) {
        dbHelper.logoutUpdate(0);
    }

    @Override
    public void resetSetting() {
        DBHelper dbHelper = new DBHelper(context);
        Socket socket = IntentData.getInstance().getSocket();

        //안드로이드 DB 초기화
        dbHelper.delete();

        // 온도, ph 초기화
        socket.emit("insertTemper",20, 28);
        socket.emit("insertPH", 6.5, 8.5);

        //먹이값 초기화
        socket.emit("insertFeed", -100, 1);

        //환수 초기화
//        timerWater = "\"" + yearWater + "/" + monthWater + "/" + dayWater + "/" + hourWater + "/" + minunteWater + "\"";
        String timerWater = "\"2000/1/1/1/1\"";
        socket.emit("insertWater", timerWater);
    }
}
