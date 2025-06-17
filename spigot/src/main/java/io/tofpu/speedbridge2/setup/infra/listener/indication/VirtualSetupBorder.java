package io.tofpu.speedbridge2.setup.infra.listener.indication;

import io.github.revxrsal.eventbus.SubscribeEvent;
import io.tofpu.speedbridge2.SpeedbridgePlugin;
import io.tofpu.speedbridge2.arena.Vector;
import io.tofpu.speedbridge2.setup.domain.event.SetupStartEvent;
import io.tofpu.speedbridge2.setup.domain.event.SetupStopEvent;
import io.tofpu.speedbridge2.setup.service.IslandSetup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VirtualSetupBorder {
    private final Map<UUID, BukkitTask> borderTasks = new HashMap<>();

    @SubscribeEvent
    public void on(SetupStartEvent event) {
        IslandSetup setup = event.getSetup();
        Player player = setup.player();

        Vector minimumPoint = setup.region().getMinimumPoint();
        Vector maximumPoint = setup.region().getMaximumPoint();

        BukkitTask bukkitTask = Bukkit.getServer().getScheduler().runTaskTimer(getPlugin(), () -> {
            VirtualBorder border = new VirtualBorder(minimumPoint, maximumPoint);
            border.send(event.getSetup().player());
        }, 0L, 20L);// Run every second
        this.borderTasks.put(player.getUniqueId(), bukkitTask);
    }

    @SubscribeEvent
    public void on(SetupStopEvent event) {
        BukkitTask bukkitTask = this.borderTasks.remove(event.getSetup().player().getUniqueId());
        if (bukkitTask != null) {
            bukkitTask.cancel();
        }
    }

    private static @NotNull JavaPlugin getPlugin() {
        return SpeedbridgePlugin.getProvidingPlugin(SpeedbridgePlugin.class);
    }
}
