package com.github.tofpu.speedbridge2.common.gameextra;

import com.github.tofpu.speedbridge2.common.gameextra.state.StopGameState;
import io.github.tofpu.speedbridge.gameengine.Game;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameRegistry<G extends Game<?>> {
    private final Map<UUID, G> ongoingGameMap = new HashMap<>();

    public void register(@NotNull UUID playerId, @NotNull G game) {
        ongoingGameMap.put(playerId, game);
    }

    @Nullable
    public G getByPlayer(UUID id) {
        G game = this.ongoingGameMap.get(id);
        if (game == null) {
            return null;
        }

        // todo: log this as automatic game correction; as this is abnormal behavior
        if (game.stateType() instanceof StopGameState) {
            removeByPlayer(id);
            return null;
        }
        return game;

    }

    public void removeByPlayer(UUID id) {
        this.ongoingGameMap.remove(id);
    }

    public boolean isInGame(UUID playerId) {
        return ongoingGameMap.containsKey(playerId);
    }
}
