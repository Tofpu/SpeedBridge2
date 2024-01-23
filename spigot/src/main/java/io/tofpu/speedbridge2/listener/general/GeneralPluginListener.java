package io.tofpu.speedbridge2.listener.general;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.listener.GameListener;
import io.tofpu.speedbridge2.model.island.arena.ArenaManager;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldInitEvent;

@AutoRegister
public class GeneralPluginListener extends GameListener {
    private final ArenaManager arenaManager;

    public GeneralPluginListener(final ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldInit(final WorldInitEvent event) {
        final World world = event.getWorld();
        final World arenaManagerWorld = arenaManager.getWorld();
        if (arenaManagerWorld == null || !world.getName()
                .equals(arenaManagerWorld
                        .getName())) {
            return;
        }
        world.setKeepSpawnInMemory(false);
    }
}
