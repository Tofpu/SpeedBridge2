package io.tofpu.speedbridge2.listener.game;

import io.tofpu.speedbridge2.domain.BridgePlayer;
import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.game.GamePlayer;
import io.tofpu.speedbridge2.domain.misc.Score;
import io.tofpu.speedbridge2.domain.service.PlayerService;
import io.tofpu.speedbridge2.listener.GameListener;
import io.tofpu.speedbridge2.util.BridgeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

public final class IslandInteractionListener extends GameListener {

    @EventHandler
    private void onBlockPlace(final BlockPlaceEvent event) {
        final GamePlayer gamePlayer = GamePlayer.of(event.getPlayer());

        if (!gamePlayer.isPlaying()) {
            return;
        }

        if (gamePlayer.hasTimerStarted()) {
            final ItemStack itemStack = event.getItemInHand();
            event.getPlayer().setItemInHand(itemStack);
            return;
        }

        gamePlayer.startTimer();
        event.getPlayer().sendMessage("started timer!");
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        final Player player = event.getPlayer();
        final GamePlayer gamePlayer = GamePlayer.of(event.getPlayer());
        if (!gamePlayer.isPlaying() || !gamePlayer.hasTimerStarted()) {
            return;
        }

        final Island island = gamePlayer.getCurrentGame().getIsland();
        final long startedAt = gamePlayer.getTimer();
        final long capturedAt = System.nanoTime();

        final double seconds = (double) (capturedAt - startedAt) / 1_000_000_000;
        final Score score = new Score(island.getSlot(), seconds);

        final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(player.getUniqueId());
        bridgePlayer.setScoreIfLower(island.getSlot(), score.getScore());

        player.sendMessage("you scored " + BridgeUtil.toFormattedScore(score.getScore()) + " seconds!");

        gamePlayer.getCurrentGame().resetGame();
    }
}
