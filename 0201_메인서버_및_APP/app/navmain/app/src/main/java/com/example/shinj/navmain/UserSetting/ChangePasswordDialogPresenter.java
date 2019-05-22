package com.example.shinj.navmain.UserSetting;

public interface ChangePasswordDialogPresenter {

    public void changePassword(String previewPw, String forwardPw);

    interface View {
        public void changePasswordResult(int result);
    }
}
