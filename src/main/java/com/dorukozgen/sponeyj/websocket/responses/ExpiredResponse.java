package com.dorukozgen.sponeyj.websocket.responses;

public class ExpiredResponse {
    private boolean expired;

    private String message;

    private String path;

    public ExpiredResponse() {}

    public ExpiredResponse(boolean expired, String message, String path) {
        this.expired = expired;
        this.message = message;
        this.path = path;
    }

    public boolean getExpired() {
        return this.expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}