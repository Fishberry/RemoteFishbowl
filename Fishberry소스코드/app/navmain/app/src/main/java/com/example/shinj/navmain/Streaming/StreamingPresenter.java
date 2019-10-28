package com.example.shinj.navmain.Streaming;

import android.webkit.WebView;

import io.socket.client.Socket;

public interface StreamingPresenter {

    void reqTimerFeed(Socket socket);
    void reqTimerWater(Socket socket);
    void reqTemperPHMsg(Socket socket);
    void startWebView(WebView webView, String address);

    interface View {

    }
}
