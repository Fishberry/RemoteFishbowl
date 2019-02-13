package com.example.shinj.navmain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FullStreammingActivity extends BaseActivity  {

    WebView webView;            //웹뷰 객체
    WebSettings webSettings;    //웹뷰 세팅 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_streamming);

        //웹뷰 객체 레이아웃 아이디와 매칭 및 설정
        webView = (WebView) findViewById(R.id.full_webview);
        webView.setWebViewClient(new WebViewClient());
        webSettings = webView.getSettings();    //웹뷰의 세팅을 웹뷰세팅 객체에게 받는다.
        webSettings.setJavaScriptEnabled(true);     //자바스크립트 사용 가능
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        //원하는 URL 됨.
        webView.loadUrl("http://fishberry.iptime.org:8080?action=stream");
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }
}
