package io.tofpu.speedbridge2.domain.player;

import io.tofpu.speedbridge2.domain.common.PlayerNameCache;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.common.database.wrapper.Database;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.common.util.DatabaseUtil;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.misc.stat.PlayerStat;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.tofpu.speedbridge2.domain.common.util.DatabaseUtil.runAsync;

public final class PlayerDatabase extends Database {
    public PlayerDatabase() {
        super(DatabaseTable.of("players", "uid text PRIMARY KEY", "name text NOT NULL"));
    }

    public @NotNull CompletableFuture<Void> insert(final @NotNull BridgePlayer player) {
        final CompletableFuture<?>[] completableFutures = new CompletableFuture[2];

        completableFutures[0] = DatabaseUtil.databaseQueryExecute(
                "INSERT OR IGNORE " + "INTO players VALUES (?, " +
                "?)", databaseQuery -> {
                    databaseQuery.setString(player.getPlayerUid()
                            .toString());
                    databaseQuery.setString(player.getPlayer()
                            .getName());
                });

        completableFutures[1] = Databases.BLOCK_DATABASE.insert(player);

        return CompletableFuture.allOf(completableFutures);
    }

    public @NotNull CompletableFuture<Void> update(final @NotNull BridgePlayer player) {
        final List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        BridgeUtil.debug("player uid: " + player.getPlayerUid());

        completableFutures.add(updateName(player.getPlayer()
                .getName(), player));
        completableFutures.add(Databases.BLOCK_DATABASE.update(player));

        for (final Score score : player.getScores()) {
            completableFutures.add(Databases.SCORE_DATABASE.update(player.getPlayerUid(), score));
        }
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
    }

    public CompletableFuture<Void> updateName(final String newName, final BridgePlayer bridgePlayer) {
        return runAsync(() -> {
            try (final DatabaseQuery databaseQuery = new DatabaseQuery(
                    "UPDATE players SET name = ? WHERE uid" + " = ?")) {
                databaseQuery.setString(newName);
                databaseQuery.setString(bridgePlayer.getPlayerUid()
                        .toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public @NotNull CompletableFuture<Void> delete(final @NotNull UUID uuid) {
        return DatabaseUtil.databaseQueryExecute("DELETE FROM players WHERE uid = ?", databaseQuery -> {
            databaseQuery.setString(uuid.toString());
        });
    }

    public @NotNull CompletableFuture<BridgePlayer> getStoredPlayer(final @NotNull UUID uniqueId) {
        return runAsync(() -> {
            final BridgePlayer bridgePlayer = BridgePlayer.of(uniqueId);

            try (final DatabaseQuery query = new DatabaseQuery(
                    "SELECT * FROM players " + "where uid = ?")) {
                query.setString(uniqueId.toString());

                final AtomicBoolean pause = new AtomicBoolean(false);
                query.executeQuery(databaseSet -> {
                    if (!databaseSet.next()) {
                        pause.set(true);
                        insert(bridgePlayer);
                    } else {
                        final String name = databaseSet.getString("name");

                        if (name != null && !name.isEmpty()) {
                            // storing the player name to the name cache
                            PlayerNameCache.INSTANCE.insert(uniqueId, name);
                        }

                        bridgePlayer.setName(name);
                    }
                });

                if (pause.get()) {
                    return bridgePlayer;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                final Collection<Score> scoreList = Databases.SCORE_DATABASE.getStoredScore(uniqueId)
                        .get();

                final Collection<PlayerStat> playerStats = Databases.STATS_DATABASE.getStoredStats(bridgePlayer.getPlayerUid())
                        .get();

                Material material =
                        Databases.BLOCK_DATABASE.getStoredMaterial(uniqueId).get();

                // if the returned material is null
                if (material == null) {
                    // reassign the material with the default block
                    material = ConfigurationManager.INSTANCE.getBlockMenuCategory().getDefaultBlock();
                    // insert the player to the blocks database
                    Databases.BLOCK_DATABASE.insert(bridgePlayer);
                }

                for (final Score score : scoreList) {
                    bridgePlayer.setInternalNewScore(score);
                }

                for (final PlayerStat playerStat : playerStats) {
                    bridgePlayer.setInternalStat(playerStat);
                }

                bridgePlayer.setIntervalMaterial(material);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            BridgeUtil.debug("successfully loaded " + uniqueId + " player's data!");

            return bridgePlayer;
        });
    }
}
