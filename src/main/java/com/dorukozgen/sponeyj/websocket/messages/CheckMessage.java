package com.dorukozgen.sponeyj.websocket.messages;

public class CheckMessage {
    private String key;

    public CheckMessage() {}

    public CheckMessage(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
