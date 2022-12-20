package io.tofpu.speedbridge2.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The default implementation of {@link ExecutionResult} interface.
 */
public class DefaultExecutionResult implements ExecutionResult {
    private final PreparedStatement preparedStatement;

    DefaultExecutionResult(final PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    @Override
    public void returnAllSet(final Consumer<ResultRetrieval> resultSetConsumer) {
        DefaultStatementQuery.assertPreparedStatementNotNull(preparedStatement);

        try {
            ResultPointer.of(preparedStatement.getResultSet())
                    .whileNext(resultSetConsumer);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public <R> R returnSingleSet(final Function<ResultRetrieval, R> resultSetFunction) {
        DefaultStatementQuery.assertPreparedStatementNotNull(preparedStatement);

        try {
            return resultSetFunction.apply(ResultPointer.of(preparedStatement.getResultSet()).next());
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
