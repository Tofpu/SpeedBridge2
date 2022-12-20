package io.tofpu.speedbridge2.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * It's responsible for encapsulating {@link ResultSet}'s pointer functionality.
 * In other words, it's in charge of pointing to data.
 * @see StatementQuery
 * @see ExecutionResult
 * @see ResultRetrieval
 */
public interface ResultPointer {
    static ResultPointer of(final ResultSet resultSet) {
        return new DefaultResultPointer(resultSet);
    }

    ResultRetrieval next() throws SQLException;
    void whileNext(Consumer<ResultRetrieval> resultRetrievalConsumer) throws SQLException;
}
