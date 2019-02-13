package com.example.shinj.navmain;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import io.socket.client.IO;
import io.socket.client.Socket;

public class StreamingActivity extends BaseActivity {

    WebView webView;            //웹뷰 객체
    WebSettings webSettings;    //웹뷰 세팅 객체
    private Socket socket;
    private TextView tempValue, phValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tempValue = (TextView) findViewById(R.id.TempValue);
        phValue = (TextView) findViewById(R.id.pHValue);

        try {
            socket = IO.socket("http://fishberry.iptime.org:3000/");
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {
                    try {
                        socket.emit("reqMsg", "App에서 측정값 받아갑니다");
                        socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                        }).on("serverMsg", (Object... objects) -> {
                            runOnUiThread(()-> {
                                tempValue.setText(objects[0].toString());
                                phValue.setText(objects[1].toString());
                            });
                        });
                        Thread.sleep(5000);
                    } catch (Exception e) {

                    }
                }
            }
        });
        t.start();

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
        webView.loadUrl("http://fishberry.iptime.org:8080/?action=stream");

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_streaming;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        socket.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        socket.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
}
