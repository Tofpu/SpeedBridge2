package io.tofpu.speedbridge2.domain.common;

import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public final class PlayerNameCache {
  public static final PlayerNameCache INSTANCE = new PlayerNameCache();

  private final Map<UUID, String> nameMap = new HashMap<>();

  /**
   * This method caches the player's name
   *
   * @param uuid player unique identification
   * @param name player name
   */
  public void insert(final UUID uuid, final String name) {
    this.nameMap.put(uuid, name);
  }

  /**
   * Retrieves the player's name synchronously
   *
   * @param uid player unique identification
   * @return the player name immediately if cached, otherwise it'll retrieve the name from the
   *     "players" table database in sync
   */
  public String getOrDefault(final UUID uid) {
    return nameMap.computeIfAbsent(uid, PlayerNameCache::getPlayerName);
  }

  /**
   * Retrieves the player's name asynchronously
   *
   * @param uid player unique identification
   * @return the player name immediately if cached, otherwise it'll retrieve the name from the
   *     "players" table database in async
   */
  public CompletableFuture<String> getOrDefaultAsync(final UUID uid) {
    final CompletableFuture<String> future = new CompletableFuture<>();

    final String result = nameMap.get(uid);
    if (result == null) {
      // offset the retrieve operation to another thread
      PluginExecutor.runAsync(
          () -> {
            future.complete(getPlayerName(uid));
          });
    } else {
      future.complete(result);
    }

    return future;
  }

  private static String getPlayerName(final UUID uuid) {
    final AtomicReference<String> result = new AtomicReference<>("");
    try (final DatabaseQuery databaseQuery =
        DatabaseQuery.query("SELECT * FROM " + "players " + "where uid = ?")) {
      databaseQuery.setString(uuid.toString());

      databaseQuery.executeQuery(
          databaseSet -> {
            if (databaseSet.next()) {
              result.set(databaseSet.getString("name"));
            }
          });
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result.get();
  }
}
