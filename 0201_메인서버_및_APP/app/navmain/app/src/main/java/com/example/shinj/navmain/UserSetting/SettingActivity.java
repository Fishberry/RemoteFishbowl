package com.example.shinj.navmain.UserSetting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shinj.navmain.DB.DBHelper;
import com.example.shinj.navmain.Login.LoginActivity;
import com.example.shinj.navmain.R;

public class SettingActivity extends AppCompatActivity implements SettingPresenter.View {

    GridView gridView;
    ConstraintLayout layout;

    private Integer[] mThumbnail = {
            R.drawable.ic_change_password,
            R.drawable.ic_reset_setting,
            R.drawable.ic_confirm_ip,
            R.drawable.ic_logout
    };

    private String[] mTitle = {
            "계정 비밀번호 변경",
            "전체설정 초기화",
            "서버 IP 확인",
            "계정 로그아웃"
    };

    final DBHelper dbHelper = new DBHelper(this);
    SettingPresenterImpl settingPresenterimpl = new SettingPresenterImpl(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        layout = findViewById(R.id.setting_layout);
        gridView = (GridView) findViewById(R.id.setting_gridview);
        Adapter adapter = new Adapter(this);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        ChangePasswordDialog dialog = new ChangePasswordDialog(SettingActivity.this);
                        dialog.callChangePasswordDialog();
                        dialog.setChangePasswordDialogListener(new ChangePasswordDialog.ChangePasswordDialogListener() {
                            @Override
                            public void onOkClicked(int result) {
                                switch (result) {
                                    case 0:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(SettingActivity.this, "이전 비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        break;
                                    case 1:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(SettingActivity.this, "변경 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        break;
                                }
                            }
                        });
                        break;
                    case 1:
                        AlertDialog.Builder resetBuilder = new AlertDialog.Builder(SettingActivity.this);
                        resetBuilder.setTitle("전체설정 초기화")
                                .setMessage("모든 설정을 초기화합니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        settingPresenterimpl.resetSetting();
                                        Toast.makeText(SettingActivity.this, "초기화를 완료했습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                        break;
                    case 2:
                        String ip = dbHelper.getResult().getIp();

                        AlertDialog.Builder iPBuilder = new AlertDialog.Builder(SettingActivity.this);
                        iPBuilder.setTitle("서버 IP 확인")
                                .setMessage("서버 IP : " + ip)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                        break;
                    case 3:
//                        dbHelper.delete();
                        settingPresenterimpl.logoutDBUpdate(dbHelper);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    class Adapter extends BaseAdapter {

        private final Activity context;

        public Adapter(Activity context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return mThumbnail.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.gridview_setting_activity, null, true);
            ImageView imageView = (ImageView) item.findViewById(R.id.gridView_setting_image);
            TextView title = (TextView) item.findViewById(R.id.gridView_setting_text);

            title.setText(mTitle[i]);
            imageView.setImageResource(mThumbnail[i]);

            return item;
        }
    }
}
