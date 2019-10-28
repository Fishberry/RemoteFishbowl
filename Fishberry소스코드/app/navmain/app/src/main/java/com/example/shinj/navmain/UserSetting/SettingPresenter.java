package com.example.shinj.navmain.UserSetting;

import com.example.shinj.navmain.DB.DBHelper;

public interface SettingPresenter {

    void logoutDBUpdate(DBHelper dbHelper);
    void resetSetting();

    interface View {

    }
}
