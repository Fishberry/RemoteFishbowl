package com.example.shinj.navmain.NotificationSetting;

import com.example.shinj.navmain.DBElement;
import com.example.shinj.navmain.DBHelper;

interface NotificationSettingPresenter {

    void getDBElement(DBHelper dbHelper);

    interface View {
        void freshDBElement(DBElement dbElement);
    }
}
