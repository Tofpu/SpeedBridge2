package io.tofpu.speedbridge2.model.island.object;

import com.google.common.base.Preconditions;
import io.tofpu.speedbridge2.model.island.IslandFactory;
import org.bukkit.Location;

public class IslandBuilder {
    private int slot = -1;
    private String category;
    private String schematic;
    private Location absoluteLocation;

    private IslandBuilder() {
        // prevent instantiation
    }

    public static IslandBuilder of() {
        return new IslandBuilder();
    }

    public IslandBuilder setSlot(final int slot) {
        this.slot = slot;
        return this;
    }

    public IslandBuilder setCategory(final String category) {
        this.category = category;
        return this;
    }

    public IslandBuilder setSchematic(final String schematic) {
        this.schematic = schematic;
        return this;
    }

    public IslandBuilder setAbsoluteLocation(final Location absoluteLocation) {
        this.absoluteLocation = absoluteLocation;
        return this;
    }

    public Island build() {
        Preconditions.checkArgument(slot >= 0, "slot must be set");
        Preconditions.checkArgument(category != null, "category must be set");
        Preconditions.checkArgument(schematic != null, "schematic must be set");
        Preconditions.checkArgument(
                absoluteLocation != null, "absoluteLocation must be set");

        return IslandFactory.create(slot, category, schematic, absoluteLocation);
    }
}
