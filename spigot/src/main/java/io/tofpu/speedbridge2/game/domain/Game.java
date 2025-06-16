package io.tofpu.speedbridge2.game.domain;

import io.tofpu.speedbridge2.arena.Arena;
import io.tofpu.speedbridge2.game.GamePlayer;
import io.tofpu.speedbridge2.island.domain.Island;
import org.bukkit.Location;
import org.bukkit.entity.Player;

// todo: handle game logic here
//  - add toolbar items
//  - add game listeners
public class Game {
    private final GamePlayer gamePlayer;
    private final Island island;
    private final Arena arena;

    private GameStateType state = GameStateType.INITIALIZE;

    public Game(
            Player player,
            Island island,
            Arena arena) {
        this.gamePlayer = new GamePlayer(player, this);
        this.island = island;
        this.arena = arena;
    }

    public void setState(GameStateType state) {
        this.state = state;
    }

    public Location location() {
        return island().positionOrientation().add(arena.getPosition()).toLocation(arena().world());
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
}
