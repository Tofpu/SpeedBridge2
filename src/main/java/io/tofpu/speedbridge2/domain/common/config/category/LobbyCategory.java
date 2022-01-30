package io.tofpu.speedbridge2.domain.common.config.category;

import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import org.bukkit.Location;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.concurrent.CompletableFuture;

@ConfigSerializable
public final class LobbyCategory {
    @Setting("teleport-on-join")
    @Comment("If this is set to true, the players will teleport to the lobby upon them " +
             "joining")
    private boolean teleportOnJoin = true;

    public boolean isTeleportOnJoin() {
        return teleportOnJoin;
    }

    @Setting("lobby-location")
    @Comment("This is where the players will teleport when they join, or leave an " +
             "island")
    private Location lobbyLocation = null;

    public CompletableFuture<Void> setLobbyLocation(final Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
        return ConfigurationManager.INSTANCE.update();
    }

    public void setTeleportOnJoin(final boolean teleportOnJoin) {
        this.teleportOnJoin = teleportOnJoin;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }
}
