package com.example.shinj.navmain.Feed;

import io.socket.client.Socket;

public class FeedFragPresenterImpl implements FeedFragPresenter {

    FeedFragPresenter.View view;

    @Override
    public void startFeed(Socket socket) {
        socket.emit("reqFeedNow", "StartServo1");
    }

    @Override
    public void saveFeed(Socket socket, int timerFeed, int circleFeed) {
        socket.emit("insertFeed", timerFeed, circleFeed);
    }
}
