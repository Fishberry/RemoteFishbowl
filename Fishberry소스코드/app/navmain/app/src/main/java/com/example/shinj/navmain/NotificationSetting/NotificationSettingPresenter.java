package com.example.shinj.navmain.NotificationSetting;

import com.example.shinj.navmain.DB.DBElement;
import com.example.shinj.navmain.DB.DBHelper;

interface NotificationSettingPresenter {

    void getDBElement(DBHelper dbHelper);

    interface View {
        void freshDBElement(DBElement dbElement);
    }
}
