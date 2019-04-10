package com.example.shinj.navmain.Streaming;

import android.webkit.WebView;

import io.socket.client.Socket;

public class StreamingPresenterImpl implements StreamingPresenter {

    @Override
    public void reqTimerFeed(Socket socket) {
        socket.emit("reqTimerFeed", "DB의 TimerFeed값 요청");
    }

    @Override
    public void reqTimerWater(Socket socket) {
        socket.emit("reqTimerWater", "DB의 TimerWater값 요청");
    }

    @Override
    public void reqTemperMsg(Socket socket) {
        socket.emit("reqMsg", "App에서 측정값 받아갑니다");
    }

    @Override
    public void startWebView(WebView webView, String address) {
        //원하는 URL 됨.
        webView.loadUrl("http://" + address + ":8080/?action=stream");
    }
}
