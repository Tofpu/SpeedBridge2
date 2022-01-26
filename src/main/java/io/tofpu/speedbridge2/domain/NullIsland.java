package io.tofpu.speedbridge2.domain;

import io.tofpu.speedbridge2.command.subcommand.SpeedBridgeCommand;
import io.tofpu.speedbridge2.domain.game.GameIsland;
import io.tofpu.speedbridge2.domain.game.GamePlayer;
import io.tofpu.speedbridge2.util.BridgeUtil;

import java.util.AbstractMap;
import java.util.Map;

import static io.tofpu.speedbridge2.command.subcommand.SpeedBridgeCommand.ERROR;

public class NullIsland extends Island {
    private static final String NOT_IN_A_ISLAND = ERROR + "You're not on an island!";

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
