package com.example.shinj.navmain;

import android.content.Intent;
import android.os.Bundle;

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
}