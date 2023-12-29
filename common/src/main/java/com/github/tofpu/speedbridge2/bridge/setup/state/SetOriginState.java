package com.github.tofpu.speedbridge2.bridge.setup.state;

import com.github.tofpu.speedbridge2.bridge.setup.BridgeSetupHandler;
import com.github.tofpu.speedbridge2.bridge.setup.IslandSetupData;
import com.github.tofpu.speedbridge2.game.state.StartGameState;
import com.github.tofpu.speedbridge2.game.Game;
import com.github.tofpu.speedbridge2.game.GameState;
import com.github.tofpu.speedbridge2.object.Location;

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
        System.out.println("gameState=" + game.state() + " (" + (game.state() instanceof StartGameState) + ")");
        return game.state() instanceof StartGameState;
    }
}
