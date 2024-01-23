package io.tofpu.speedbridge2.model.common.config.serializer;

import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Arrays;

public final class LocationSerializer implements TypeSerializer<Location> {
    public static final LocationSerializer INSTANCE = new LocationSerializer();

    private LocationSerializer() {}

    @Override
    public Location deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final String worldName = nonVirtualNode(node, "world").getString();

        // if the world name is nonexistent, return the default
        // location
        if (worldName == null) {
            return defaultLocation();
        }

        final World world = getWorld(worldName);


        // if the world was not loaded before, or it was deleted
        // then we will create/load it manually
//        if (world == null) {
//            world = Bukkit.createWorld(WorldCreator.name(worldName));
//        }

        final double x = nonVirtualNode(node, "x").getDouble();
        final double y = nonVirtualNode(node, "y").getDouble();
        final double z = nonVirtualNode(node, "z").getDouble();
        final float yaw = nonVirtualNode(node, "yaw").getFloat();
        final float pitch = nonVirtualNode(node, "pitch").getFloat();

        return new Location(world, x, y, z, yaw, pitch);
    }

    private World getWorld(final String worldName) {
        // attempts to get a world that are already loaded
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            return world;
        }

        // loads/creates a world with the given worldName
        // and notifies the administrators of the given action
        BridgeUtil.log("Loading " + worldName + " world for the speedbridge2 lobby!");
        return Bukkit.createWorld(WorldCreator.name(worldName));
    }

    private ConfigurationNode nonVirtualNode(final ConfigurationNode source, final Object... path) throws SerializationException {
        if (!source.hasChild(path)) {
            throw new SerializationException("Required field " + Arrays.toString(path) +
                                             " was not present in node");
        }
        return source.node(path);
    }

    private Location defaultLocation() {
        final World world;
        try {
            world = Bukkit.getWorlds()
                    .get(0);
            return world.getSpawnLocation();
        } catch (final NullPointerException ignored) {
            // do nothing
        }
        return null;
    }

    @Override
    public void serialize(final Type type, @Nullable Location obj, final ConfigurationNode node) throws SerializationException {
        if (obj == null && (obj = defaultLocation()) == null) {
            node.raw(null);
            return;
        }
        node.node("world")
                .set(obj.getWorld()
                        .getName());
        node.node("x")
                .set(obj.getX());
        node.node("y")
                .set(obj.getY());
        node.node("z")
                .set(obj.getZ());
        node.node("yaw")
                .set(obj.getYaw());
        node.node("pitch")
                .set(obj.getPitch());
    }
}
