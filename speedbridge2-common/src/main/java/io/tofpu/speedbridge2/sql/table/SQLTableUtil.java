package io.tofpu.speedbridge2.sql.table;

/**
 * A SQL-table convenience utility class.
 */
public class SQLTableUtil {
    public static String tableAsSQLFormat(final RepositoryTable repositoryTable) {
        return "CREATE TABLE IF NOT EXISTS " + repositoryTable.getTableName() + " " + parameterAsString(repositoryTable);
    }

    private static String parameterAsString(final RepositoryTable repositoryTable) {
        final StringBuilder parameterBuilder = new StringBuilder("(");
        for (final String parameter : repositoryTable.getTableParameters()) {
            parameterBuilder.append(parameter).append(", ");
        }
        parameterBuilder.delete(parameterBuilder.length() - 2, parameterBuilder.length()).append(")");
        return parameterBuilder.toString();
    }
}
