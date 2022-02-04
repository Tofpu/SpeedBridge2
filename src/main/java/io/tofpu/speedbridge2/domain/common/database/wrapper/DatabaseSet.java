package io.tofpu.speedbridge2.domain.common.database.wrapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class DatabaseSet {
    private final ResultSet resultSet;

    public DatabaseSet(final ResultSet resultSet) {
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

    public String getString(final String column) {
        try {
            return resultSet.getString(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "null";
    }

    public int getInt(final String column) {
        try {
            return resultSet.getInt(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public double getDouble(final String column) {
        try {
            return resultSet.getDouble(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
