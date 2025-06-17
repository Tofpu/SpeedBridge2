package io.tofpu.speedbridge2.setup.infra.listener.indication;

import io.tofpu.speedbridge2.arena.Vector;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public record VirtualBorder(
        Vector minimumPoint, Vector maximumPoint
) {
    public void send(Player player) {
        AxisType.ALL.forEach(axisType -> new TargetAxis(axisType, minimumPoint, maximumPoint)
                .forEach(player.getWorld(), location -> spawnPracticle(player, location)));
    }

    private static void spawnPracticle(Player player, Location location) {
        player.spawnParticle(SettingsHolder.particle, location, SettingsHolder.count, SettingsHolder.options);
    }

    public static class SettingsHolder {
        public static Particle particle = Particle.REDSTONE;
        public static Object options = new Particle.DustOptions(Color.RED, 3);
        public static int count = 1;
    }
}
