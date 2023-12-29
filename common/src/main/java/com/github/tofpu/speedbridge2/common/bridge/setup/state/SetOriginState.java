package com.github.tofpu.speedbridge2.common.bridge.setup.state;

import com.github.tofpu.speedbridge2.common.bridge.setup.IslandSetupData;
import com.github.tofpu.speedbridge2.common.game.Game;
import com.github.tofpu.speedbridge2.common.game.GameState;
import com.github.tofpu.speedbridge2.common.game.GameStateTag;
import com.github.tofpu.speedbridge2.common.game.state.BasicGameStateTag;
import com.github.tofpu.speedbridge2.object.Location;
import org.jetbrains.annotations.NotNull;

class SetOriginState implements GameState<IslandSetupData> {
    private final SetupStateProvider stateHandler;
    private final Location origin;

    SetOriginState(SetupStateProvider stateHandler, Location origin) {
        this.stateHandler = stateHandler;
        this.origin = origin;
    }

    @Override
    public void apply(Game<IslandSetupData> game) {
        IslandSetupData data = game.data();
        Location subtracted = data.land().getIslandLocation().subtract(origin)
                .setYaw(origin.getYaw()).setPitch(origin.getPitch());
        data.origin(subtracted);
        System.out.println("Setting island's setup location to " + subtracted);
        game.dispatch(stateHandler.stopState());
    }

    @Override
    public boolean test(Game<IslandSetupData> game) {
        System.out.println("gameState=" + game.state() + " (" + (game.state().getClass().getSimpleName()) + ")");
        return game.state().tag() == BasicGameStateTag.STARTED;
    }

    @Override
    public @NotNull GameStateTag tag() {
        return BridgeSetupStateTag.SET_ORIGIN;
    }
}
