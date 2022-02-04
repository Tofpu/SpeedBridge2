package io.tofpu.speedbridge2.domain.common.database.wrapper;

import io.tofpu.speedbridge2.domain.common.database.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class DatabaseQuery implements AutoCloseable {
    private final PreparedStatement preparedStatement;
    private final AtomicInteger setterCounter;

    public DatabaseQuery(final String query) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = DatabaseManager.getConnection().prepareStatement(query);
        } catch (final SQLException exception) {
            exception.printStackTrace();
            preparedStatement = null;
        }
        this.preparedStatement = preparedStatement;
        this.setterCounter = new AtomicInteger(0);
    }

    public DatabaseQuery setInt(final Integer integer) {
        try {
            this.preparedStatement.setInt(setterCounter.incrementAndGet(), integer);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return this;
    }

    public DatabaseQuery setString(final String string) {
        try {
            this.preparedStatement.setString(setterCounter.incrementAndGet(), string);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return this;
    }

    public DatabaseQuery setDouble(final double score) {
        try {
            this.preparedStatement.setDouble(setterCounter.incrementAndGet(), score);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return this;
    }

    public DatabaseQuery setLong(final long l) {
        try {
            this.preparedStatement.setLong(setterCounter.incrementAndGet(), l);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return this;
    }

    public boolean execute() {
        try {
            return this.preparedStatement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public void executeQuery(final Consumer<ResultSet> resultSetConsumer) {
        try {
            try (final ResultSet resultSet = this.preparedStatement.executeQuery()) {
                resultSetConsumer.accept(resultSet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        if (this.preparedStatement == null) {
            return;
        }
        this.preparedStatement.getConnection().close();
        this.preparedStatement.close();
    }
}
