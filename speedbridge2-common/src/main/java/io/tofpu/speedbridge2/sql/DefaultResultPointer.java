package io.tofpu.speedbridge2.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * The default implementation of {@link ResultPointer} interface.
 */
public class DefaultResultPointer implements ResultPointer {
    private final ResultSet resultSet;

    DefaultResultPointer(final ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public ResultRetrieval next() throws SQLException {
        if (resultSet.next()) {
            return ResultRetrieval.of(resultSet);
        }
        return null;
    }

    @Override
    public void whileNext(final Consumer<ResultRetrieval> resultRetrievalConsumer) throws SQLException {
        while (resultSet.next()) {
            resultRetrievalConsumer.accept(ResultRetrieval.of(resultSet));
        }
    }
}
