package io.tofpu.speedbridge2.domain.player.misc.block;

import io.tofpu.speedbridge2.domain.common.database.wrapper.Database;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.domain.common.util.DatabaseUtil;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public final class BlockDatabase extends Database {
    public BlockDatabase() {
        super(DatabaseTable.of("blocks", "uid text PRIMARY KEY", "chosen_block text NOT NULL"));
    }

    public CompletableFuture<Void> insert(final @NotNull BridgePlayer player) {
        return DatabaseUtil.databaseQueryExecute("INSERT OR IGNORE INTO blocks VALUES (?, ?)", databaseQuery -> {
            databaseQuery.setString(player.getPlayerUid()
                    .toString());
            databaseQuery.setString(player.getChoseMaterial()
                    .name());
        });
    }

    public CompletableFuture<Void> update(final @NotNull BridgePlayer player) {
        return DatabaseUtil.databaseQueryExecute("UPDATE blocks SET chosen_block = ? " +
                                                 "WHERE uid = ?", databaseQuery -> {
            databaseQuery.setString(player.getChoseMaterial()
                    .name());
            databaseQuery.setString(player.getPlayerUid()
                    .toString());
        });
    }

    public CompletableFuture<Material> getStoredMaterial(final @NotNull UUID uniqueId) {
        return DatabaseUtil.runAsync(() -> {
            final AtomicReference<Material> material = new AtomicReference<>(null);

            try (final DatabaseQuery databaseQuery = new DatabaseQuery(
                    "SELECT * FROM " + "blocks WHERE " + "uid = ?")) {
                databaseQuery.setString(uniqueId.toString());

                databaseQuery.executeQuery(databaseSet -> {
                    if (!databaseSet.next()) {
                        return;
                    }

                    final String name = databaseSet.getString("chosen_block");
                    if (name == null || name.isEmpty()) {
                        return;
                    }

                    material.set(Material.matchMaterial(name));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return material.get();
        });
    }
}
