package com.github.tofpu.speedbridge2.bridge.setup;

import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.bridge.core.state.StartGameState;
import com.github.tofpu.speedbridge2.object.Location;

class SetOriginState implements Game.GameState {
    private final BridgeSetupHandler setupHandler;
    private final Location origin;

    SetOriginState(BridgeSetupHandler setupHandler, Location origin) {
        this.setupHandler = setupHandler;
        this.origin = origin;
    }

    @Override
    public void apply(Game game) {
        apply((IslandSetup) game);
    }

    public void apply(IslandSetup game) {
        Location subtracted = game.land().getIslandLocation().subtract(origin)
                .setYaw(origin.getYaw()).setPitch(origin.getPitch());
        game.origin(subtracted);
        System.out.println("Setting island's setup location to " + subtracted);
        setupHandler.stop(game.gamePlayer().id());
    }

    @Override
    public boolean test(Game game) {
        System.out.println("gameState=" + game.gameState() + " (" + (game.gameState() instanceof StartGameState) + ")");
        return game.gameState() instanceof StartGameState;
    }
}
