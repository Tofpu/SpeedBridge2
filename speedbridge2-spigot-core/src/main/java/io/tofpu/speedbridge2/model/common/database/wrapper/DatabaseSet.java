package io.tofpu.speedbridge2.model.common.database.wrapper;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class DatabaseSet {
    private final @NotNull ResultSet resultSet;

    public DatabaseSet(final @NotNull ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public boolean next() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getRow() {
        try {
            return resultSet.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getString(final @NotNull String column) {
        try {
            return resultSet.getString(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getInt(final @NotNull String column) {
        try {
            return resultSet.getInt(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getInt(final int index) {
        try {
            return resultSet.getInt(index);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public double getDouble(final @NotNull String column) {
        try {
            return resultSet.getDouble(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
