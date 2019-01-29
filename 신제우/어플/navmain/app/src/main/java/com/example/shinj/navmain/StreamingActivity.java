package com.example.shinj.navmain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import io.socket.client.IO;
import io.socket.client.Socket;

public class StreamingActivity extends AppCompatActivity {

    WebView webView;            //웹뷰 객체
    WebSettings webSettings;    //웹뷰 세팅 객체
    private Socket socket;
    private TextView tempValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);

        tempValue = (TextView) findViewById(R.id.TempValue);

        try {
            socket = IO.socket("http://192.168.0.114:3000");
            socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
            }).on("serverMessage", (Object... objects) -> {
                runOnUiThread(()->{
                    tempValue.setText(objects[0].toString());
                });
            });
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //웹뷰 객체 레이아웃 아이디와 매칭 및 설정
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webSettings = webView.getSettings();    //웹뷰의 세팅을 웹뷰세팅 객체에게 받는다.
        webSettings.setJavaScriptEnabled(true);     //자바스크립트 사용 가능

        //원하는 URL 됨.
        //webView.loadUrl("URL");
    }
}
