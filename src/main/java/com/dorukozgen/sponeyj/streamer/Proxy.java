package com.dorukozgen.sponeyj.streamer;

public final class Proxy {
    private final String host;

    private final int port;

    private final String username;

    private final String password;

    private final Type type;

    public enum Type {
        HTTP(2),
        SOCKS(4);

        private final int value;

        Type(int newValue) {
            this.value = newValue;
        }

        public int getValue() {
            return this.value;
        }
    }

    public Proxy(String host, int port, String username, String password, int type) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        if (type == 2) {
            this.type = Type.HTTP;
        } else {
            this.type = Type.SOCKS;
        }
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Type getType() {
        return this.type;
    }
}
