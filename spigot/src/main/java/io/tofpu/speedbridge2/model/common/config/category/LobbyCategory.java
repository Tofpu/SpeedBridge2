package io.tofpu.speedbridge2.model.common.config.category;

import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import org.bukkit.Location;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.concurrent.CompletableFuture;

@ConfigSerializable
@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public final class LobbyCategory {
    @Setting("teleport-on-join")
    @Comment("If this is set to true, the players will teleport to the lobby upon them " +
            "joining")
    private boolean teleportOnJoin = true;
    @Setting("lobby-location")
    @Comment("This is where the players will teleport when they join, or leave an " +
            "island")
    private Location lobbyLocation = null;

    @Comment("If this is set to true, the player's inventory will be cleared after joining")
    private boolean clearInventoryOnJoin = true;

    public boolean isTeleportOnJoin() {
        return teleportOnJoin;
    }

    public boolean clearInventoryOnJoin() {
        return clearInventoryOnJoin;
    }

    public CompletableFuture<Void> setLobbyLocation(final Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
        return ConfigurationManager.INSTANCE.update();
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }
}
