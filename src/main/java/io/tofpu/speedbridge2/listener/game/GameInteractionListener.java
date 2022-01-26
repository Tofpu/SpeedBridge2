package io.tofpu.speedbridge2.listener.game;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.player.misc.Score;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import io.tofpu.speedbridge2.listener.GameListener;
import io.tofpu.speedbridge2.listener.wrapper.wrappers.BlockPlaceEventWrapper;
import io.tofpu.speedbridge2.listener.wrapper.wrappers.PlayerInteractEventWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import static io.tofpu.speedbridge2.domain.common.Message.SCORED;
import static io.tofpu.speedbridge2.domain.common.Message.TIME_STARTED;

@AutoRegister
public final class GameInteractionListener extends GameListener {
    @EventHandler
    private void whenPlayerPlaceBlock(final BlockPlaceEventWrapper eventWrapper) {
        final GamePlayer gamePlayer = eventWrapper.getGamePlayer();

        final BlockPlaceEvent event = eventWrapper.getEvent();
        if (gamePlayer.hasTimerStarted()) {
            final ItemStack itemStack = event.getItemInHand();
            event.getPlayer()
                    .setItemInHand(itemStack);
            return;
        }

        gamePlayer.startTimer();
        BridgeUtil.sendMessage(event.getPlayer(), TIME_STARTED);
    }

    @EventHandler
    private void whenPlayerScore(final PlayerInteractEventWrapper eventWrapper) {
        final BridgePlayer bridgePlayer = eventWrapper.getBridgePlayer();
        final GamePlayer gamePlayer = eventWrapper.getGamePlayer();

        final Island island = gamePlayer.getCurrentGame()
                .getIsland();
        final long startedAt = gamePlayer.getTimer();
        final long capturedAt = System.nanoTime();

        final double seconds = (double) (capturedAt - startedAt) / 1_000_000_000;
        final Score score = new Score(island.getSlot(), seconds);

        bridgePlayer.setScoreIfLower(island.getSlot(), score.getScore());

        BridgeUtil.sendMessage(bridgePlayer, String.format(SCORED, BridgeUtil.toFormattedScore(score.getScore())));

        gamePlayer.getCurrentGame().resetGame(false);
    }
}
