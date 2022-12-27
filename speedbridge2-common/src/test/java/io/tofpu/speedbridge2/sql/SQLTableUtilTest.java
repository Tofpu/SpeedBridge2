package io.tofpu.speedbridge2.sql;

import io.tofpu.speedbridge2.sql.table.DefaultRepositoryTable;
import io.tofpu.speedbridge2.sql.table.RepositoryTable;
import io.tofpu.speedbridge2.sql.table.SQLTableUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SQLTableUtilTest {
    @Test
    public void test_table_as_sql_format() {
        final RepositoryTable repositoryTable = new DefaultRepositoryTable(
                "profiles",
                "id UNIQUE PRIMARY KEY",
                "name TEXT NOT NULL");

        final String actual = "CREATE TABLE IF NOT EXISTS profiles (id UNIQUE PRIMARY KEY, name TEXT NOT NULL)";
        assertEquals(SQLTableUtil.tableAsSQLFormat(repositoryTable), actual);
    }
}
