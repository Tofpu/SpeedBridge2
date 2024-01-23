package com.github.tofpu.speedbridge2.common.bridge.game.listener;

import com.github.tofpu.speedbridge2.common.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.common.bridge.game.*;
import com.github.tofpu.speedbridge2.common.bridge.game.event.IslandGamePrepareEvent;
import com.github.tofpu.speedbridge2.common.bridge.game.event.IslandGameResetEvent;
import com.github.tofpu.speedbridge2.common.bridge.game.event.IslandGameStopEvent;
import com.github.tofpu.speedbridge2.common.bridge.game.event.PlayerScoredEvent;
import com.github.tofpu.speedbridge2.common.bridge.game.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.common.gameextra.land.GameLandReserver;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.event.Listener;
import com.github.tofpu.speedbridge2.event.dispatcher.EventListener;
import com.github.tofpu.speedbridge2.event.dispatcher.ListeningState;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;

import java.util.IllegalFormatException;

public class GameListener implements Listener {
    private final PlatformGameAdapter gameAdapter;
    private final BridgeScoreService scoreService;
    private final LobbyService lobbyService;
    private final GameLandReserver landReserver;

    public GameListener(PlatformGameAdapter gameAdapter, BridgeScoreService scoreService, LobbyService lobbyService, GameLandReserver landReserver) {
        this.gameAdapter = gameAdapter;
        this.scoreService = scoreService;
        this.lobbyService = lobbyService;
        this.landReserver = landReserver;
    }

    @EventListener(state = ListeningState.MONITORING, ignoreCancelled = true)
    public void on(IslandGamePrepareEvent event)  {
        IslandGame game = event.game();
        gameAdapter.onGamePrepare(game, event.player());
        game.dispatch(BridgeStateTypes.START);
    }

    @EventListener(state = ListeningState.MONITORING, ignoreCancelled = true)
    public void on(IslandGameResetEvent event) {
        IslandGame game = event.game();
        gameAdapter.onGameReset(game, event.player());

        System.out.println("IslandResetGameState -- dispatching GameStartedState");
        game.dispatch(BridgeStateTypes.START);
    }

    @EventListener(state = ListeningState.MONITORING, ignoreCancelled = true)
    public void on(PlayerScoredEvent event) {
        String finalMessage = event.scoreMessage();
        IslandGame game = event.game();
        IslandGameData data = game.data();
        double timerInSeconds = data.timerInSeconds();
        try {
            finalMessage = String.format(event.scoreMessage(), timerInSeconds);
        } catch (IllegalFormatException ignored) {
        }

        IslandGamePlayer player = event.player();
        scoreService.addScore(player.id(), data.getIsland().getSlot(), timerInSeconds);

        event.player().getPlayer().sendMessage(finalMessage);
        game.dispatch(BridgeStateTypes.RESET);
    }

    @EventListener(state = ListeningState.MONITORING, ignoreCancelled = true)
    public void on(IslandGameStopEvent event) {
        IslandGamePlayer player = event.player();
        OnlinePlayer onlinePlayer = player.getPlayer();

        onlinePlayer.teleport(lobbyService.position());
        landReserver.releaseSpot(player.id());

        IslandGame game = event.game();
        IslandGameData data = game.data();
        gameAdapter.onGameStop(game, player);
        onlinePlayer.sendMessage("Game ended within " + data.timerInSeconds() + " seconds, bravo!");
    }
}
