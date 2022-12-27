package io.tofpu.speedbridge2.sql;

import java.sql.ResultSet;

/**
 * It's responsible for encapsulating {@link ResultSet}'s retrieval process.
 * In other words, it's in charge of handling the data's retrieval process.
 *
 * @see StatementQuery
 * @see ExecutionResult
 * @see ResultPointer
 */
public interface ResultRetrieval {
    static ResultRetrieval of(final ResultSet resultSet) {
        return new DefaultResultRetrieval(resultSet);
    }

    String getString(String label);
    int getInteger(String label);
    double getDouble(String label);
}
