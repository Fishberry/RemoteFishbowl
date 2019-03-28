package com.example.shinj.navmain;

public class DBElement {
    String ip = "";
    int isRememberIP = 0;
    int watchElement = 0;
    int temperLoopTime = 0;
    int pHLoopTime = 0;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getWatchElement() {
        return watchElement;
    }

    public void setWatchElement(int watchElement) {
        this.watchElement = watchElement;
    }

    public int getIsRememberIP() {
        return isRememberIP;
    }

    public void setIsRememberIP(int isRememberIP) {
        this.isRememberIP = isRememberIP;
    }

    public int getTemperLoopTime() {
        return temperLoopTime;
    }

    public void setTemperLoopTime(int temperLoopTime) {
        this.temperLoopTime = temperLoopTime;
    }

    public int getpHLoopTime() {
        return pHLoopTime;
    }

    public void setpHLoopTime(int pHLoopTime) {
        this.pHLoopTime = pHLoopTime;
    }
}
