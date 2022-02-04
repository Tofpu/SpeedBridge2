package io.tofpu.speedbridge2.listener.game;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.common.Message;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.misc.stat.PlayerStatType;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import io.tofpu.speedbridge2.listener.GameListener;
import io.tofpu.speedbridge2.listener.wrapper.wrappers.BlockPlaceEventWrapper;
import io.tofpu.speedbridge2.listener.wrapper.wrappers.PlayerInteractEventWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public final class GameInteractionListener extends GameListener {
    @EventHandler
    private void whenPlayerPlaceBlock(final @NotNull BlockPlaceEventWrapper eventWrapper) {
        final GamePlayer gamePlayer = eventWrapper.getGamePlayer();

        final BlockPlaceEvent event = eventWrapper.getEvent();
        if (gamePlayer.hasTimerStarted()) {
            final ItemStack itemStack = event.getItemInHand();
            event.getPlayer()
                    .setItemInHand(itemStack);
            return;
        }

        gamePlayer.startTimer();
        BridgeUtil.sendMessage(event.getPlayer(), Message.INSTANCE.TIME_STARTED);
    }

    @EventHandler
    private void whenPlayerScore(final @NotNull PlayerInteractEventWrapper eventWrapper) {
        final BridgePlayer bridgePlayer = eventWrapper.getBridgePlayer();
        final GamePlayer gamePlayer = eventWrapper.getGamePlayer();

        final Island island = gamePlayer.getCurrentGame()
                .getIsland();
        final long startedAt = gamePlayer.getTimer();

        final double seconds = BridgeUtil.nanoToSeconds(startedAt);
        final Score score = new Score(island.getSlot(), seconds);

        bridgePlayer.setScoreIfLower(island.getSlot(), score.getScore());
        bridgePlayer.increment(PlayerStatType.TOTAL_WINS);

        BridgeUtil.sendMessage(bridgePlayer, String.format(Message.INSTANCE.SCORED, BridgeUtil.formatNumber(score.getScore())));

        gamePlayer.getCurrentGame().resetGame(false);
    }
}
