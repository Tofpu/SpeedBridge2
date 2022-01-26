package io.tofpu.speedbridge2.domain.island.object;

import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;

import java.util.AbstractMap;
import java.util.Map;

import static io.tofpu.speedbridge2.domain.common.Message.NOT_IN_A_ISLAND;

public class NullIsland extends Island {
    public NullIsland() {
        super(-1, "null");
    }

    @Override
    public Map.Entry<GamePlayer, GameIsland> generateGame(final BridgePlayer player) {
        return new AbstractMap.SimpleImmutableEntry<>(null, null);
    }

    @Override
    public void leaveGame(final BridgePlayer bridgePlayer) {
        BridgeUtil.sendMessage(bridgePlayer.getPlayer(), NOT_IN_A_ISLAND);
    }

    @Override
    public GameIsland findGameByPlayer(final GamePlayer gamePlayer) {
        return null;
    }

    @Override
    public void setCategory(final String anotherCategory) {}

    @Override
    public boolean selectSchematic(final String schematicName) {
        return false;
    }
}
