package com.dorukozgen.sponeyj.websocket.messages;

public class StartMessage {
    private String[] songs;

    private String threadCount;

    private boolean useAccounts;

    private boolean useProxy;

    private int proxyType;

    public StartMessage() {}

    public StartMessage(String[] songs, String threadCount, boolean useAccounts, boolean useProxy, int proxyType) {
        this.songs = songs;
        this.threadCount = threadCount;
        this.useAccounts = useAccounts;
        this.useProxy = useProxy;
        this.proxyType = proxyType;
    }

    public String[] getSongs() {
        return this.songs;
    }

    public void setSongs(String[] songs) {
        this.songs = songs;
    }

    public String getThreadCount() {
        return this.threadCount;
    }

    public void setThreadCount(String threadCount) {
        this.threadCount = threadCount;
    }

    public boolean getUseAccounts() {
        return this.useAccounts;
    }

    public void setUseAccounts(boolean useAccounts) {
        this.useAccounts = useAccounts;
    }

    public boolean getUseProxy() {
        return this.useProxy;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    public int getProxyType() {
        return this.proxyType;
    }

    public void setProxyType(int proxyType) {
        this.proxyType = proxyType;
    }
}
