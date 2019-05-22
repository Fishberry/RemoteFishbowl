package com.example.shinj.navmain.UserSetting;

import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shinj.navmain.R;

public class ChangePasswordDialog implements ChangePasswordDialogPresenter.View {

    private Context context;
    private ChangePasswordDialogListener changePasswordDialogListener;
    Dialog dlg;

    public ChangePasswordDialog(Context context) {
        this.context = context;
    }

    public interface ChangePasswordDialogListener {
        public void onOkClicked(int result);
    }

    public void setChangePasswordDialogListener(ChangePasswordDialogListener changePasswordDialogListener) {
        this.changePasswordDialogListener = changePasswordDialogListener;
    }

    public void callChangePasswordDialog() {

        dlg = new Dialog(context);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dialog_change_password);
        dlg.show();

        EditText previewEdit = (EditText) dlg.findViewById(R.id.preview_password_edit);
        EditText forwardEdit = (EditText) dlg.findViewById(R.id.forward_password_edit);
        TextView okText = (TextView) dlg.findViewById(R.id.ok_text);
        TextView cancelText = (TextView) dlg.findViewById(R.id.cancel_text);
        View okButton = (View) dlg.findViewById(R.id.ok_button);
        View cancelButton = (View) dlg.findViewById(R.id.cancel_button);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChangePasswordDialogImpl impl = new ChangePasswordDialogImpl(ChangePasswordDialog.this);

                String previewPw = previewEdit.getText().toString();
                String forwardPw = forwardEdit.getText().toString();

                impl.changePassword(previewPw, forwardPw);
            }
        });
        okText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChangePasswordDialogImpl impl = new ChangePasswordDialogImpl(ChangePasswordDialog.this);

                String previewPw = previewEdit.getText().toString();
                String forwardPw = forwardEdit.getText().toString();

                impl.changePassword(previewPw, forwardPw);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }

    @Override
    public void changePasswordResult(int result) {

        changePasswordDialogListener.onOkClicked(result);
        dlg.dismiss();
    }
}
