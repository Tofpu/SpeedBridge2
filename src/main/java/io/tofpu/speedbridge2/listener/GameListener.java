package io.tofpu.speedbridge2.listener;

import io.tofpu.speedbridge2.plugin.SpeedBridgePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class GameListener implements Listener {
  public GameListener() {
    Bukkit.getPluginManager()
        .registerEvents(this, SpeedBridgePlugin.getPlugin(SpeedBridgePlugin.class));
  }
}
