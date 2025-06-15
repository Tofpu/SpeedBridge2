package io.tofpu.speedbridge2.game;

import io.tofpu.speedbridge2.arena.Arena;
import io.tofpu.speedbridge2.game.config.experience.GamePlayerExperienceConfiguration;
import io.tofpu.speedbridge2.game.state.AbstractGameState;
import io.tofpu.speedbridge2.game.state.GameStateProvider;
import io.tofpu.speedbridge2.game.state.GameStateType;
import io.tofpu.speedbridge2.island.domain.Island;
import org.bukkit.entity.Player;

// todo: handle game logic here
//  - add toolbar items
//  - add game listeners
public class Game {
    private final GamePlayer gamePlayer;
    private final Island island;
    private final Arena arena;
    private final GamePlayerExperienceConfiguration experienceConfiguration;
    private final GameStateProvider stateProvider;

    private AbstractGameState state = AbstractGameState.identity(this);

    public Game(
            Player player,
            Island island,
            Arena arena,
            GamePlayerExperienceConfiguration experienceConfiguration,
            GameStateProvider stateProvider) {
        this.gamePlayer = new GamePlayer(player, this);
        this.island = island;
        this.arena = arena;
        this.experienceConfiguration = experienceConfiguration;
        this.stateProvider = stateProvider;
    }

    public void setState(GameStateType state) {
        if (this.state.type().equals(state)) {
            return;
        }
        this.state = stateProvider.provideState(state, this);
        this.state.handle();
    }

    public Island island() {
        return island;
    }

    public Arena arena() {
        return arena;
    }

    public GamePlayer gamePlayer() {
        return gamePlayer;
    }

    public GameStateProvider stateProvider() {
        return stateProvider;
    }

    public GamePlayerExperienceConfiguration experienceConfiguration() {
        return experienceConfiguration;
    }
}
