package io.tofpu.speedbridge2.model.player;

import com.google.common.base.Preconditions;
import io.tofpu.speedbridge2.model.island.IslandService;
import io.tofpu.speedbridge2.model.leaderboard.Leaderboard;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.DummyBridgePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class PlayerFactory {
    private static IslandService islandService;
    private static Leaderboard leaderboard;

    public static void init(final @NotNull IslandService islandService, final @NotNull Leaderboard leaderboard) {
        PlayerFactory.islandService = islandService;
        PlayerFactory.leaderboard = leaderboard;
    }

    public static BridgePlayer create(final String name, final UUID uuid) {
        return create(PlayerFactoryType.REGULAR, name, uuid);
    }

    public static BridgePlayer create(final UUID uuid) {
        return create(PlayerFactoryType.REGULAR, uuid);
    }

    public static BridgePlayer createDummy(final String name, final UUID uuid) {
        return create(PlayerFactoryType.DUMMY, name, uuid);
    }

    public static BridgePlayer createDummy(final UUID uuid) {
        return create(PlayerFactoryType.DUMMY, uuid);
    }

    public static BridgePlayer create(final PlayerFactoryType type, final String name,
                                      final UUID uuid) {
        Preconditions.checkNotNull(name, "name cannot be null");
        Preconditions.checkNotNull(uuid, "uuid cannot be null");

        switch (type) {
            case REGULAR:
                return BridgePlayer.of(islandService, leaderboard, name, uuid);
            case DUMMY:
                return DummyBridgePlayer.of(islandService, leaderboard, name, uuid);
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    public static BridgePlayer create(final PlayerFactoryType type,
                                      final UUID uuid) {
        Preconditions.checkNotNull(uuid, "uuid cannot be null");

        switch (type) {
            case REGULAR:
                return BridgePlayer.of(islandService, leaderboard, uuid);
            case DUMMY:
                return DummyBridgePlayer.of(islandService, leaderboard, uuid);
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    public enum PlayerFactoryType {
        REGULAR,
        DUMMY;
    }
}
