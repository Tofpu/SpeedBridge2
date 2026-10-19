package io.tofpu.speedbridge2.model.common.database.wrapper;

import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import org.jetbrains.annotations.NotNull;

public class DatabaseTable {
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s (%s)";
    private final String table;
    private final String[] columns;
    private final String sql;

    private DatabaseTable(final @NotNull String table, final @NotNull String... columns) {
        this.table = table;
        this.columns = columns;
        this.sql = String.format(CREATE_TABLE, table, formatColumns(columns));

        BridgeUtil.debug(sql);
    }

    public static DatabaseTable of(final @NotNull String table,
                                   final @NotNull String... columns) {
        return new DatabaseTable(table, columns);
    }

    public @NotNull String formatColumns(final String[] columns) {
        final StringBuilder formattedColumn = new StringBuilder();

        int index = 0;
        for (final String column : columns) {
            if (index != 0) {
                formattedColumn.append(", ");
            }
            formattedColumn.append(column);
            index++;
        }

        return formattedColumn.toString();
    }

    public @NotNull String getTable() {
        return table;
    }

    public @NotNull String[] getColumns() {
        return columns;
    }

    public @NotNull String getSql() {
        return sql;
    }

    @Override
    public @NotNull String toString() {
        return getSql();
    }
}
