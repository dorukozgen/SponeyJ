package com.dorukozgen.sponeyj.websocket.responses;

public class StartResponse {
    private boolean success;

    private String message;

    public StartResponse() {}

    public StartResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}