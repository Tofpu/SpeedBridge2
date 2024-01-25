package com.github.tofpu.speedbridge2.common.setup;

import com.github.tofpu.speedbridge2.common.gameextra.GameRegistry;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.Land;
import com.github.tofpu.speedbridge2.common.setup.state.StartSetupState;
import com.github.tofpu.speedbridge2.common.setup.state.StopSetupState;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import io.github.tofpu.speedbridge.gameengine.BaseGameHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

class IslandSetupHandler extends BaseGameHandler<IslandSetupData> {
    private final EventDispatcherService eventDispatcher;

    private final GameRegistry<IslandSetup> gameRegistry = new GameRegistry<>();

    public IslandSetupHandler(EventDispatcherService eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
        registerStates();
    }

    @Override
    public void registerStates() {
        this.stateManager.addState(IslandSetupStates.START, new StartSetupState(eventDispatcher));
        this.stateManager.addState(IslandSetupStates.STOP, new StopSetupState(eventDispatcher));
    }

    public void start(final OnlinePlayer player, final int slot, final String schematicName, final Land land) {
        if (gameRegistry.isInGame(player.id())) {
            return;
        }
        IslandSetup islandSetup = createSetup(player, slot, schematicName, land);
        islandSetup.dispatch(IslandSetupStates.START);
        gameRegistry.register(player.id(), islandSetup);
    }

    @NotNull
    private IslandSetup createSetup(OnlinePlayer player, int slot, String schematicName, Land land) {
        IslandSetupPlayer gamePlayer = new IslandSetupPlayer(player);
        IslandSetupData gameData = new IslandSetupData(gamePlayer, slot, schematicName, land);
        return new IslandSetup(gameData, stateManager);
    }

    public void stopSetup(final UUID playerId) {
        IslandSetup game = getSetupByPlayer(playerId);
        if (game == null) return;

        // sometimes this method is called by the event since anybody
        // could dispatch the stop state, not just this method
        if (game.stateType() != IslandSetupStates.STOP) {
            game.dispatch(IslandSetupStates.STOP);
        }
        gameRegistry.removeByPlayer(playerId);
    }
    @Nullable
    public IslandSetup getSetupByPlayer(UUID playerId) {
        return gameRegistry.getByPlayer(playerId);
    }
}
