package com.github.tofpu.speedbridge2.common.setup;

import com.github.tofpu.speedbridge2.common.gameextra.GameRegistry;
import com.github.tofpu.speedbridge2.common.gameextra.land.Land;
import com.github.tofpu.speedbridge2.common.setup.state.SetOriginState;
import com.github.tofpu.speedbridge2.common.setup.state.StartSetupState;
import com.github.tofpu.speedbridge2.common.setup.state.StopSetupState;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import io.github.tofpu.speedbridge.gameengine.BaseGameHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BridgeSetupHandler extends BaseGameHandler<IslandSetupData> {
    private final EventDispatcherService eventDispatcher;

    private final GameRegistry<IslandSetup> gameRegistry = new GameRegistry<>();

    public BridgeSetupHandler(EventDispatcherService eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
        registerStates();
    }

    @Override
    public void registerStates() {
        this.stateManager.addState(IslandSetupStates.START, new StartSetupState());
        this.stateManager.addState(IslandSetupStates.STOP, new StopSetupState(eventDispatcher));
        this.stateManager.addState(IslandSetupStates.SET_ORIGIN, new SetOriginState());
    }

    public void start(final OnlinePlayer player, final int slot, final String schematicName, final Land land) {
        if (gameRegistry.isInGame(player.id())) {
            return;
        }
        IslandSetup game = createGame(player, slot, schematicName, land);
        game.dispatch(IslandSetupStates.START);
    }

    @NotNull
    private IslandSetup createGame(OnlinePlayer player, int slot, String schematicName, Land land) {
        SetupPlayer gamePlayer = new SetupPlayer(player);
        IslandSetupData gameData = new IslandSetupData(gamePlayer, slot, schematicName, land);
        return new IslandSetup(gameData, stateManager);
    }

    public void stopGame(final UUID playerId) {
        IslandSetup game = getGameByPlayer(playerId);
        if (game == null) return;

        game.dispatch(IslandSetupStates.STOP);
        gameRegistry.removeByPlayer(playerId);
    }
    @Nullable
    public IslandSetup getGameByPlayer(UUID playerId) {
        return gameRegistry.getByPlayer(playerId);
    }
}
