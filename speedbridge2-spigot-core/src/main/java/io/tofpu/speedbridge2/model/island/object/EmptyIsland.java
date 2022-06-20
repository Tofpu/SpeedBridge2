package io.tofpu.speedbridge2.model.island.object;

import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.GamePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Map;

public class EmptyIsland extends Island {
    public EmptyIsland() {
        super(null, null, -1, "null");
    }

    @Override
    public Map.Entry<GamePlayer, GameIsland> join(final BridgePlayer player) {
        return new AbstractMap.SimpleImmutableEntry<>(null, null);
    }

    @Override
    public void leaveGame(final BridgePlayer bridgePlayer) {
        BridgeUtil.sendMessage(bridgePlayer.getPlayer(),
                Message.INSTANCE.notInAIsland);
    }

    @Override
    public GameIsland findGameByPlayer(final GamePlayer gamePlayer) {
        return null;
    }

    @Override
    public void delete() {
        // does nothing
    }

    @Override
    public void setCategory(final String anotherCategory) {
        // does nothing
    }

    @Override
    public boolean selectSchematic(final @NotNull String schematicName) {
        return false;
    }
}
