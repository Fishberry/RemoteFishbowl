package com.example.shinj.navmain.DB;

public class DBElement {
    public String ip = "";
    public int isRememberIP = 0;
    public int watchElement = 0;
    public int temperLoopTime = 0;
    public int pHLoopTime = 0;

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
