package com.github.tofpu.speedbridge2.bridge.setup;

import com.github.tofpu.speedbridge2.bridge.core.state.StartGameState;
import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.bridge.core.GameState;
import com.github.tofpu.speedbridge2.object.Location;

class SetOriginState implements GameState<IslandSetupData> {
    private final BridgeSetupHandler setupHandler;
    private final Location origin;

    SetOriginState(BridgeSetupHandler setupHandler, Location origin) {
        this.setupHandler = setupHandler;
        this.origin = origin;
    }

    @Override
    public void apply(Game<IslandSetupData> game) {
        IslandSetupData data = game.data();
        Location subtracted = data.land().getIslandLocation().subtract(origin)
                .setYaw(origin.getYaw()).setPitch(origin.getPitch());
        data.origin(subtracted);
        System.out.println("Setting island's setup location to " + subtracted);
        setupHandler.stop(data.player().id());

    }

    @Override
    public boolean test(Game<IslandSetupData> game) {
        System.out.println("gameState=" + game.state() + " (" + (game.state() instanceof StartGameState) + ")");
        return game.state() instanceof StartGameState;
    }
}
