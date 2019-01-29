package com.example.shinj.navfrag;

import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class FragActivity extends AppCompatActivity implements View.OnClickListener{
    Button bt1,bt2;
    FragmentManager fm;
    FragmentTransaction tran;
    FeedFrag feedFrag;
    TemperFrag temperFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        feedFrag = new FeedFrag();
        temperFrag = new TemperFrag();
        setFrag(0);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt1:
                setFrag(0);
                break;
            case R.id.bt2:
                setFrag(1);
                break;
        }
    }
    public void setFrag(int n){    //프래그먼트를 교체하는 작업을 하는 메소드
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (n){
            case 0:
                tran.replace(R.id.frag_frame, feedFrag);
                tran.commit();
                break;
            case 1:
                tran.replace(R.id.frag_frame, temperFrag);
                tran.commit();
                break;
        }
    }
}