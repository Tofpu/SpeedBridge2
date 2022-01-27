package io.tofpu.speedbridge2.domain.common.database.wrapper;

public class DatabaseTable {
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s (%s)";
    private final String table;
    private final String[] columns;
    private final String sql;

    public static DatabaseTable of(final String table, final String... columns) {
        return new DatabaseTable(table, columns);
    }

    private DatabaseTable(final String table, final String... columns) {
        this.table = table;
        this.columns = columns;
        this.sql = String.format(CREATE_TABLE, table, formatColumns(columns));

        System.out.println(sql);
    }

    public String formatColumns(final String[] columns) {
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

    public String getTable() {
        return table;
    }

    public String[] getColumns() {
        return columns;
    }

    public String getSql() {
        return sql;
    }

    @Override
    public String toString() {
        return getSql();
    }
}
