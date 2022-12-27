package io.tofpu.speedbridge2.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The default implementation of {@link ResultRetrieval} interface.
 */
public class DefaultResultRetrieval implements ResultRetrieval {
    private final ResultSet resultSet;

    DefaultResultRetrieval(final ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public String getString(final String label) {
        try {
            return resultSet.getString(label);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int getInteger(final String label) {
        try {
            return resultSet.getInt(label);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public double getDouble(final String label) {
        try {
            return resultSet.getDouble(label);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
