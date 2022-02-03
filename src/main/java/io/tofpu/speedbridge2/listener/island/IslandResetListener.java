package io.tofpu.speedbridge2.listener.island;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.listener.GameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

@AutoRegister
public final class IslandResetListener extends GameListener {
    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        // TODO: remove the bridge player eventually!
        final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(event.getPlayer()
                .getUniqueId());
        if (bridgePlayer == null || !bridgePlayer.isPlaying()) {
            return;
        }

        // quit from the game
        bridgePlayer.getGamePlayer().getCurrentGame().getIsland().leaveGame(bridgePlayer);
    }
}
