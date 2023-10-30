package com.github.tofpu.speedbridge2.object;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@NoArgsConstructor(force = true)
@Data
public class Location {
    @Embedded
    private final World world;
    private final int x, y, z;
    private final float yaw, pitch;

    public static Location zero(World world) {
        return new Location(world, 0, 0, 0, 0, 0);
    }

    public Location(World world, int x, int y, int z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location setYaw(float yaw) {
        return new Location(this.world, this.x, this.y, this.z, yaw, pitch);
    }

    public Location setPitch(float pitch) {
        return new Location(this.world, this.x, this.y, this.z, yaw, pitch);
    }

    public Location subtract(Location other) {
        return new Location(this.world, this.x - other.x, this.y - other.y, this.z - other.z, this.yaw, this.pitch);
    }
}
