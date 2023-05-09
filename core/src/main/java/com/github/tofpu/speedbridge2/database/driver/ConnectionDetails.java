package com.github.tofpu.speedbridge2.database.driver;

public class ConnectionDetails {
    public static final ConnectionDetails MEMORY = new ConnectionDetails("", ConnectionType.MEMORY);

    private final String data;
    private final ConnectionType type;

    private ConnectionDetails(String data, ConnectionType type) {
        this.data = data;
        this.type = type;
    }

    public static ConnectionDetails file(String data) {
        return new ConnectionDetails(data, ConnectionType.FILE);
    }

    public String getData() {
        return data;
    }

    public ConnectionType getType() {
        return type;
    }
}
