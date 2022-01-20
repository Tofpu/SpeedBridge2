package io.tofpu.speedbridge2.database.wrapper;

import io.tofpu.speedbridge2.database.manager.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseQuery implements AutoCloseable {
    private final PreparedStatement preparedStatement;

    public DatabaseQuery(final String query) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = DatabaseManager.getConnection().prepareStatement(query);
        } catch (SQLException exception) {
            exception.printStackTrace();
            preparedStatement = null;
        }
        this.preparedStatement = preparedStatement;
    }

    public DatabaseQuery setInt(final int index, final Integer integer) {
        try {
            this.preparedStatement.setInt(index, integer);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return this;
    }

    public DatabaseQuery setString(final int index, final String string) {
        try {
            this.preparedStatement.setString(index, string);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return this;
    }

    public DatabaseQuery setLong(final int index, final long l) {
        try {
            this.preparedStatement.setLong(index, l);
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

    public ResultSet executeQuery() {
        try {
            return this.preparedStatement.executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        if (this.preparedStatement == null) {
            return;
        }
        this.preparedStatement.getConnection().close();
        this.preparedStatement.close();
    }

    public DatabaseQuery setDouble(final int index, final double score) {
        try {
            this.preparedStatement.setDouble(index, score);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return this;
    }
}
