package io.tofpu.speedbridge2.plugin;

import io.tofpu.speedbridge2.domain.service.IslandService;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpeedBridgePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        final IslandService service = IslandService.INSTANCE;
        service.load();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
