package com.example.shinj.navmain;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(getApplicationContext(), StreamingActivity.class);
        startActivity(intent);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    /* 하단 백버튼 누르면 로그인화면으로 돌아가기 */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그인화면으로")
                .setMessage("로그인 화면으로 돌아가시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
}