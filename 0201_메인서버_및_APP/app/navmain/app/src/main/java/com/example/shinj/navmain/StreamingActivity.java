package com.example.shinj.navmain;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import io.socket.client.Socket;

public class StreamingActivity extends BaseActivity {

    WebView webView;            //웹뷰 객체
    WebSettings webSettings;    //웹뷰 세팅 객체
    private static Socket socket;
    private TextView tempValue, phValue, feedTimer, waterTimer, settingTemperPh;
    private int feedTimeRemain;
    private String waterTimeRemain;
    Intent extraIntent;
    String address;
    String progressTemp;
    IntentData intentData = IntentData.getInstance();
    boolean isConnectSensor;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extraIntent = getIntent();
        feedTimer = findViewById(R.id.feedTimer);
        waterTimer = findViewById(R.id.waterTimer);
        settingTemperPh = findViewById(R.id.settingTemperPh);
        tempValue = (TextView) findViewById(R.id.TempValue);
        phValue = (TextView) findViewById(R.id.pHValue);
        address = intentData.getAddress();
        socket = intentData.getSocket();
        final ProgressBar progressBarTemperature = (ProgressBar) findViewById(R.id.progresBarTemper);


        /* 서버DB에서 환수설정시간을 받아와서 출력 */
        socket.emit("reqTimerWater", "DB의 TimerWater값 요청");
        socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
        }).on("resTimerWater", (Object... objects) -> {
            try {
                waterTimeRemain = objects[0].toString();
                waterTimer.setText("환수시간: " + waterTimeRemain.split("/")[0] + "년"
                + waterTimeRemain.split("/")[1] + "월"
                + waterTimeRemain.split("/")[2] + "일"
                + waterTimeRemain.split("/")[3] + "시"
                + waterTimeRemain.split("/")[4] + "분");
            } catch (Exception e) {

            }
        });
        /* 서버DB에서 먹이급여예약시간을 받아와서 1초에 한번씩 출력하는 쓰레드 */
        Thread thread_feed = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        socket.emit("reqTimerFeed", "DB의 TimerFeed값 요청");
                        socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                        }).on("resTimerFeed", (Object... objects) -> {
                            runOnUiThread(()-> {
                                if (!(objects[0].toString().equals("0")) || !(objects[1].toString().equals("0"))) {
                                    feedTimeRemain = Integer.parseInt(objects[1].toString());
                                    feedTimer.setText("먹이급여: " + feedTimeRemain/60/60 + "시간 "
                                            + feedTimeRemain/60%60 + "분 " +
                                            + feedTimeRemain%60 + "초 "
                                            + " 회전수: " + objects[0].toString() );
                                }
                            });
                        });
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        Toast.makeText(StreamingActivity.this, "먹이급여시간 오류", Toast.LENGTH_SHORT).show();
                        setWhetherSensor(false);
                    }
                }
            }
        });
        thread_feed.start();

        /* 온도랑 pH값 5초마다 서버에서 받아와서 5초에 한번씩 출력하는 쓰레드 */
        Thread thread_temper = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        socket.emit("reqMsg", "App에서 측정값 받아갑니다");
                        socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                        }).on("serverMsg", (Object... objects) -> {
                            runOnUiThread(()-> {
                                if (!(objects[0].toString().equals("0")) || !(objects[1].toString().equals("0"))) {
                                    setWhetherSensor(true);
                                    /* 서버DB에서 온도,pH 위험설정값을 받아와서 출력 */
                                    settingTemperPh.setText("온도설정: " + objects[2].toString() + " ~ " + objects[3].toString() + "\n"
                                            + "pH설정: " + objects[4].toString() + " ~ " + objects[5].toString());
                                    //
                                    tempValue.setText(progressTemp = objects[0].toString());
                                    phValue.setText(objects[1].toString());
                                    progressTemp = progressTemp.substring(0, 2);    // 온도계 프로그래스바에 온도표시하려고 할 때 서버에서 objects[0]로 받아오는 온도값이 24.xxx처럼 돼 있어서 int형 부분만 자름
                                    // handler.post는 온도계 프로그래스바에 온도값 표시해주는 메소드
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (-40 < Integer.parseInt(progressTemp))
                                                progressBarTemperature.setProgress(Integer.parseInt(progressTemp));
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(StreamingActivity.this, "온도센서 혹은 수질센서에서 값을 받아 올 수 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        Toast.makeText(StreamingActivity.this, "온도센서 혹은 수질센서의 연결에 이상이 생겼습니다.", Toast.LENGTH_SHORT).show();
                        setWhetherSensor(false);
                    }

                    if (isConnectSensor == false)
                        break;
                }
            }
        });
        thread_temper.start();

        //웹뷰 객체 레이아웃 아이디와 매칭 및 설정
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webSettings = webView.getSettings();    //웹뷰의 세팅을 웹뷰세팅 객체에게 받는다.
        webSettings.setJavaScriptEnabled(true);     //자바스크립트 사용 가능
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webView.setLongClickable(true);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (v == webView) {

                    // 롱 클릭한 부분의 정보를 구한다

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("스트리밍 전체화면")
                            .setMessage("전체화면으로 보시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(StreamingActivity.this, FullStreammingActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).show();
                }
                return false;
            }
        });

        //원하는 URL 됨.
        webView.loadUrl("http://" + address + ":8080/?action=stream");

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("종료")
                .setMessage("Fishberry를 종료하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();           //해당 앱의 루트 액티비티 종료
                        System.runFinalization();   //현재 작업중인 쓰레드가 다 종료되면, 종료하라는 메소드
                        System.exit(0);     //현재 액티비티 종료
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.show();
    }

    void setWhetherSensor(boolean sensorBooleanValue) {
        isConnectSensor = sensorBooleanValue;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_streaming;
    }
}
