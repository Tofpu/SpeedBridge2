package io.tofpu.speedbridge2.listener.game;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.common.Message;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
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
      event.getPlayer().setItemInHand(itemStack);
      return;
    }

    gamePlayer.startTimer();
    BridgeUtil.sendMessage(event.getPlayer(), Message.INSTANCE.timeStarted);
  }

  @EventHandler
  private void whenPlayerScore(final @NotNull PlayerInteractEventWrapper eventWrapper) {
    final BridgePlayer player = eventWrapper.getBridgePlayer();

    final GameIsland currentGame = player.getCurrentGame();
    if (currentGame == null) {
      return;
    }

    final Island island = currentGame.getIsland();
    final long startedAt = player.getTimer();

    final double seconds = BridgeUtil.nanoToSeconds(startedAt);
    final Score score = new Score(island.getSlot(), seconds);

    player.setScoreIfLower(island.getSlot(), score.getScore());
    player.increment(PlayerStatType.TOTAL_WINS);

    BridgeUtil.sendMessage(
        player, String.format(Message.INSTANCE.scored, BridgeUtil.formatNumber(score.getScore())));

    currentGame.resetGame(false);
  }
}
