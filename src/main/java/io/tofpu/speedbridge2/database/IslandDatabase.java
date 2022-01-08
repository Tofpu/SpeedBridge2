package io.tofpu.speedbridge2.database;

import io.tofpu.speedbridge2.database.util.DatabaseQuery;
import io.tofpu.speedbridge2.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.domain.Island;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IslandDatabase extends Database {
    IslandDatabase() {
        super(DatabaseTable.of("islands", "slot NOT NULL PRIMARY KEY", "category TEXT"));
    }

    public void insert(final Island island) {
        try (final DatabaseQuery query = new DatabaseQuery("INSERT OR IGNORE INTO islands VALUES (?, ?)")) {
            query.setInt(1, island.getSlot());
            query.setString(2, island.getCategory());

            query.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void update(final Island island) {
        try (final DatabaseQuery query = new DatabaseQuery("UPDATE islands SET category = ? WHERE slot = ?")) {
            query.setString(1, island.getCategory());
            System.out.println("island category: " + island.getCategory());
            query.setInt(2, island.getSlot());

            query.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void delete(final int slot) {
        try (final DatabaseQuery query = new DatabaseQuery("DELETE FROM islands WHERE slot = ?")) {
            query.setInt(1, slot);

            query.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public List<Island> getStoredIslands() {
        final List<Island> islands = new ArrayList<>();

        try (final DatabaseQuery query = new DatabaseQuery("SELECT * FROM islands")) {
            final ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                islands.add(new Island(resultSet.getInt(1), resultSet.getString(2)));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return islands;
    }
}
