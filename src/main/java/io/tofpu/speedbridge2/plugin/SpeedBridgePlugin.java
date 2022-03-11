package io.tofpu.speedbridge2.plugin;

import io.tofpu.speedbridge2.SpeedBridge;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpeedBridgePlugin extends JavaPlugin {
  private final SpeedBridge speedBridge;

  public SpeedBridgePlugin() {
    this.speedBridge = new SpeedBridge(this);
  }

  @Override
  public void onEnable() {
    speedBridge.load();
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    speedBridge.shutdown();
  }
}
