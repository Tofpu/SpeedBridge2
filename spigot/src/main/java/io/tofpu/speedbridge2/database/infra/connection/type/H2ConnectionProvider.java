package io.tofpu.speedbridge2.database.infra.connection.type;

import io.tofpu.speedbridge2.database.infra.connection.ConnectionProvider;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2ConnectionProvider implements ConnectionProvider {
    private static final String BASE_URL = "jdbc:h2";

    private static String baseUrl(String restOfUrl) {
        if (restOfUrl.startsWith(":")) {
            restOfUrl = restOfUrl.substring(1);
        }
        return BASE_URL + ":" + restOfUrl + ";DB_CLOSE_DELAY=-1";
    }

    private final String url;

    public static H2ConnectionProvider memory() {
        return new H2ConnectionProvider(
                baseUrl("mem:test")
        );
    }

    public static H2ConnectionProvider file(File file) {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("File must not be a directory: " + file.getAbsolutePath());
        }
        return new H2ConnectionProvider(
                baseUrl(file.getAbsolutePath())
        );
    }

    public H2ConnectionProvider(String url) {
        this.url = url;
    }

    private final ThreadLocal<Connection> threadLocalConnection = ThreadLocal.withInitial(this::createConnection);

    @Override
    public Connection provideConnection() {
        Connection connection = threadLocalConnection.get();
        try {
            if (connection.isClosed()) {
                connection = createConnection();
                threadLocalConnection.set(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to provide H2 connection", e);
        }
        return connection;
    }

    private Connection createConnection() {
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create H2 connection", e);
        }
    }
}
