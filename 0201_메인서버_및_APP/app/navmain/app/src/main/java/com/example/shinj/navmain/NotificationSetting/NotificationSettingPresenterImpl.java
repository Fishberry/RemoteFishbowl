package com.example.shinj.navmain.NotificationSetting;

import com.example.shinj.navmain.DB.DBElement;
import com.example.shinj.navmain.DB.DBHelper;

class NotificationSettingPresenterImpl implements NotificationSettingPresenter {

    NotificationSettingPresenter.View view;
    public NotificationSettingPresenterImpl(View view) {
        this.view = view;
    }

    @Override
    public void getDBElement(DBHelper dbHelper) {
        DBElement dbElement = dbHelper.getResult();

        view.freshDBElement(dbElement);
    }
}
