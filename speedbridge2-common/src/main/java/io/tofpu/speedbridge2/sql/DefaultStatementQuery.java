package io.tofpu.speedbridge2.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.apache.commons.lang.Validate.notNull;

/**
 * The default implementation of {@link StatementQuery} interface.
 */
public class DefaultStatementQuery implements StatementQuery {
    private final Connection connection;
    private final PreparedStatement preparedStatement;

    DefaultStatementQuery(final Connection connection, final PreparedStatement prepareStatement) {
        this.connection = connection;
        this.preparedStatement = prepareStatement;
    }

    @Override
    public StatementQuery setLong(final int index, final long value) {
        assertConnectionNotNull();
        assertPreparedStatementNotNull(preparedStatement);

        try {
            preparedStatement.setLong(index, value);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return new DefaultStatementQuery(connection, preparedStatement);
    }

    @Override
    public StatementQuery setString(final int index, final String value) {
        assertConnectionNotNull();
        assertPreparedStatementNotNull(preparedStatement);

        try {
            preparedStatement.setString(index, value);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return new DefaultStatementQuery(connection, preparedStatement);
    }

    @Override
    public StatementQuery setBlob(final int index, final byte[] value) {
        assertConnectionNotNull();
        assertPreparedStatementNotNull(preparedStatement);

        try {
            preparedStatement.setBytes(index, value);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return new DefaultStatementQuery(connection, preparedStatement);
    }

    @Override
    public ExecutionResult execute() {
        assertConnectionNotNull();
        assertPreparedStatementNotNull(preparedStatement);

        try {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return ExecutionResult.of(preparedStatement);
    }

    private void assertConnectionNotNull() {
        notNull(this.connection, "Connection must not be null");
    }

    static void assertPreparedStatementNotNull(final PreparedStatement preparedStatement) {
        notNull(preparedStatement, "Query must not be null");
    }
}
