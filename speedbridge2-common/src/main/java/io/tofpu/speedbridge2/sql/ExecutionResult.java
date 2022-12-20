package io.tofpu.speedbridge2.sql;

import java.sql.PreparedStatement;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * It's responsible for encapsulating {@link PreparedStatement} execution process.
 * In other words, it's in charge of handling the execution's data.
 * <p>
 * <b>Example</b>: if a hotel were to query a user list
 * with {@link StatementQuery}.
 * This interface would be responsible for handling back
 * the user's data set results.
 *
 * @see StatementQuery
 */
public interface ExecutionResult {
    static ExecutionResult of(PreparedStatement preparedStatement) {
        return new DefaultExecutionResult(preparedStatement);
    }

    void returnAllSet(Consumer<ResultRetrieval> resultSetConsumer);
    <R> R returnSingleSet(Function<ResultRetrieval, R> resultSetFunction);
}
