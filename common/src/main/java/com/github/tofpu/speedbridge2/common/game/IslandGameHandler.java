package com.github.tofpu.speedbridge2.common.game;

import com.github.tofpu.speedbridge2.common.game.state.basic.GamePrepareState;
import com.github.tofpu.speedbridge2.common.game.state.basic.GameStartedState;
import com.github.tofpu.speedbridge2.common.game.state.basic.GameStopState;
import com.github.tofpu.speedbridge2.common.game.state.game.IslandResetGameState;
import com.github.tofpu.speedbridge2.common.game.state.game.ScoredGameState;
import com.github.tofpu.speedbridge2.common.gameextra.GameRegistry;
import com.github.tofpu.speedbridge2.common.gameextra.land.Land;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import io.github.tofpu.speedbridge.gameengine.BaseGameHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class IslandGameHandler extends BaseGameHandler<IslandGameData> {
    private final EventDispatcherService eventDispatcher;

    private final GameRegistry<IslandGame> gameRegistry = new GameRegistry<>();

    public IslandGameHandler(EventDispatcherService eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
        registerStates();
    }

    @Override
    public void registerStates() {
        this.stateManager.addState(BridgeStateTypes.PREPARE, new GamePrepareState(eventDispatcher));
        this.stateManager.addState(BridgeStateTypes.START, new GameStartedState());
        this.stateManager.addState(BridgeStateTypes.STOP, new GameStopState(eventDispatcher));

        this.stateManager.addState(BridgeStateTypes.SCORED, new ScoredGameState(eventDispatcher));
        this.stateManager.addState(BridgeStateTypes.RESET, new IslandResetGameState(eventDispatcher));
    }

    public boolean start(final OnlinePlayer player, final Island island, Land land) {
        if (gameRegistry.isInGame(player.id())) {
            return false;
        }

        IslandGame game = createGame(player, island, land);
        game.dispatch(BridgeStateTypes.PREPARE);
        gameRegistry.register(player.id(), game);
        return true;
    }

    @NotNull
    private IslandGame createGame(OnlinePlayer player, Island island, Land land) {
        return new IslandGame(new IslandGameData(new IslandGamePlayer(player), island, land), stateManager);
    }

    public boolean stop(final UUID playerId) {
        IslandGame game = getGameByPlayer(playerId);
        if (game == null) {
            return false;
        }

        game.dispatch(BridgeStateTypes.STOP);
        gameRegistry.removeByPlayer(playerId);
        return true;
    }

    public boolean isInGame(final UUID playerId) {
        return getGameByPlayer(playerId) != null;
    }

    @Nullable
    public IslandGame getGameByPlayer(UUID playerId) {
        return gameRegistry.getByPlayer(playerId);
    }
}
