package io.tofpu.speedbridge2.game.domain;

import io.tofpu.multiworldedit.VectorWrapper;
import io.tofpu.speedbridge2.arena.Arena;
import io.tofpu.speedbridge2.arena.CuboidRegion;
import io.tofpu.speedbridge2.game.GamePlayer;
import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.util.PositionOrientation;
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

    public void teleport(Player player) {
        player.teleport(location());
    }

    public void setState(GameStateType state) {
        this.state = state;
    }

    Location location() {
        System.out.println("Arena position: " + arena.getPosition());
        VectorWrapper origin = island.schematic().clipboard().getOrigin();
        System.out.println("Island origin position: [x=%s, y=%s, z=%s]".formatted(
                origin.getX(), origin.getY(), origin.getZ()
        ));
        System.out.println("Island relative position: " + island.positionOrientation());

        PositionOrientation location = arena.getPosition().subtract(island.positionOrientation());
        System.out.println("Game location (arena-island): " + location);
        return location.toLocation(arena.world());
    }

    public Island island() {
        return island;
    }

    public GamePlayer gamePlayer() {
        return gamePlayer;
    }

    public CuboidRegion region() {
        return arena.getRegion();
    }
}
