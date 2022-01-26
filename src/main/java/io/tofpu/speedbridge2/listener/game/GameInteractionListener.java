package io.tofpu.speedbridge2.listener.game;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import io.tofpu.speedbridge2.domain.player.misc.Score;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.listener.GameListener;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.common.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@AutoRegister
public final class GameInteractionListener extends GameListener {
    private static final String STYLE = "<gold>" + MessageUtil.Symbols.CLOCK.getSymbol() + "<yellow> ";
    private static final String SECOND_STYLE = "<gold>" + MessageUtil.Symbols.STAR.getSymbol() + "<yellow> ";

    private static final String TIME_STARTED = STYLE + "The timer is now ticking!";
    private static final String SCORED = SECOND_STYLE + "You scored <yellow>%s</yellow> " + "seconds!";

    @EventHandler
    private void onBlockPlace(final BlockPlaceEvent event) {
        final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(event.getPlayer()
                .getUniqueId());
        if (!bridgePlayer.isPlaying()) {
            return;
        }
        final GamePlayer gamePlayer = bridgePlayer.getGamePlayer();

        if (gamePlayer.hasTimerStarted()) {
            final ItemStack itemStack = event.getItemInHand();
            event.getPlayer().setItemInHand(itemStack);
            return;
        }

        gamePlayer.startTimer();
        BridgeUtil.sendMessage(event.getPlayer(), TIME_STARTED);
    }

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        final Player player = event.getPlayer();
        final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(player.getUniqueId());
        final GamePlayer gamePlayer = bridgePlayer.getGamePlayer();
        if (!bridgePlayer.isPlaying() || !gamePlayer.hasTimerStarted()) {
            return;
        }

        final Island island = gamePlayer.getCurrentGame().getIsland();
        final long startedAt = gamePlayer.getTimer();
        final long capturedAt = System.nanoTime();

        final double seconds = (double) (capturedAt - startedAt) / 1_000_000_000;
        final Score score = new Score(island.getSlot(), seconds);

        bridgePlayer.setScoreIfLower(island.getSlot(), score.getScore());

        BridgeUtil.sendMessage(player, String.format(SCORED, BridgeUtil.toFormattedScore(score
                .getScore())));

        gamePlayer.getCurrentGame().resetGame(false);
    }
}
