package org.minecralogy.qqbot;

public class Config {
    String uri;
    String name;
    String token;
    int reconnect_interval;
    Config(String uri, String name, String token, int reconnect_interval) {
        this.uri = uri;
        this.name = name;
        this.token = token;
        this.reconnect_interval = reconnect_interval;
    }

    public int getReconnect_interval() {
        return reconnect_interval;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public String getUri() {
        return uri;
    }
}
