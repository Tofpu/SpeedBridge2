package io.tofpu.speedbridge2.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * It's responsible for encapsulating {@link PreparedStatement} querying functionality.
 * In other words, it's in charge of handling querying data.
 * <p>
 * <b>Example</b>: Let's say we wanted to query a list of booked tickets of a specific flight
 * <pre>{@code
 *  query(connection, "SELECT * flight_tickets WHERE flight_id = ?")
 *                 .setLong(1, flightId)
 *                 .execute()
 *                 .returnAllSet(resultRetrieval -> {
 *                     // access data here...
 *                 });
 * }</pre>
 *
 * @see ExecutionResult
 * @see ResultPointer
 * @see ExecutionResult
 */
public interface StatementQuery {
    static StatementQuery query(Connection connection, String sql) {
        final PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return new DefaultStatementQuery(connection, preparedStatement);
    }

    StatementQuery setInteger(int index, int value);
    StatementQuery setDouble(int index, double value);
    StatementQuery setLong(int index, long value);
    StatementQuery setString(int index, String value);
    StatementQuery setBlob(int index, byte[] value);

    ExecutionResult execute();
}
