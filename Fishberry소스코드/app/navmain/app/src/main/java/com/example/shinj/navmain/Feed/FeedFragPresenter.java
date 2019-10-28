package com.example.shinj.navmain.Feed;

import io.socket.client.Socket;

public interface FeedFragPresenter {

    void startFeed(Socket socket);
    void saveFeed(Socket socket, int timerFeed, int circleFeed);

    interface View {

    }
}
