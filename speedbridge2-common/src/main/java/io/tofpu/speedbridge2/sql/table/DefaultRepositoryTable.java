package io.tofpu.speedbridge2.sql.table;

public class DefaultRepositoryTable implements RepositoryTable {
    private final String tableName;
    private final String[] tableParameters;

    public DefaultRepositoryTable(final String tableName, final String... tableParameters) {
        this.tableName = tableName;
        this.tableParameters = tableParameters;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String[] getTableParameters() {
        return tableParameters;
    }
}
