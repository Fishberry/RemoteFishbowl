package com.example.shinj.navmain;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    
    public void registerButtonClick(View v) {
        Toast.makeText(this, "아직 미구현이에요~!", Toast.LENGTH_SHORT).show();
    }
}
