package io.tofpu.speedbridge2.game.state.type;

import static io.tofpu.speedbridge2.util.LocationUtil.asVector;

import io.tofpu.speedbridge2.arena.Arena;
import io.tofpu.speedbridge2.arena.CuboidRegion;
import io.tofpu.speedbridge2.game.Game;
import io.tofpu.speedbridge2.game.GamePlayer;
import io.tofpu.speedbridge2.game.state.AbstractGameState;
import io.tofpu.speedbridge2.game.state.GameStateType;
import io.tofpu.speedbridge2.game.toolbar.GameEquipmentHandler;
import io.tofpu.speedbridge2.util.PositionOrientation;
import io.tofpu.speedbridge2.util.listener.ListenerRegistration;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

// todo: make the listeners global, that way each game won't need to register them
//  but until then, we'll need to register the listeners and unregister them when the game is stopped
public class GameStartState extends AbstractGameState implements Listener {

    private final GameEquipmentHandler equipmentHandler;

    public GameStartState(Game game, GameEquipmentHandler equipmentHandler) {
        super(game, GameStateType.START);
        this.equipmentHandler = equipmentHandler;
    }

    @Override
    public void registerListeners(ListenerRegistration listenerRegistration) {
        listenerRegistration.register(this);
    }

    @Override
    public void unregisterListeners(ListenerRegistration listenerRegistration) {
        listenerRegistration.unregister(this);
    }

    @Override
    public void handle() {
        Arena arena = game.arena();

        PositionOrientation gamePosition = game.island().positionOrientation().add(arena.getPosition());

        Player player = game.gamePlayer().player();
        player.teleport(gamePosition.toLocation(arena.world()));

        equipmentHandler.equip(player);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void on(PlayerMoveEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        if (!game.gamePlayer().player().getUniqueId().equals(playerId)) {
            return;
        }

        CuboidRegion region = game.arena().getRegion();
        if (!region.contains(asVector(event.getTo()))) {
            game.setState(GameStateType.RESET);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void on(BlockPlaceEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        GamePlayer gamePlayer = game.gamePlayer();
        if (!gamePlayer.player().getUniqueId().equals(playerId)) {
            return;
        }

        if (gamePlayer.hasTimerStarted()) {
            return;
        }
        gamePlayer.startTimer();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void on(PlayerInteractEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        GamePlayer gamePlayer = game.gamePlayer();
        if (!gamePlayer.player().getUniqueId().equals(playerId)) {
            return;
        }

        // making sure that the player interacted with a pressure plate
        if (event.getAction() != Action.PHYSICAL
                || !event.getClickedBlock().getType().name().contains("PLATE")) {
            return;
        }

        if (!gamePlayer.hasTimerStarted()) {
            return;
        }

        gamePlayer.endTimer();
        game.setState(GameStateType.SCORE);
    }
}
