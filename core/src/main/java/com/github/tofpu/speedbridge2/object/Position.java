package com.github.tofpu.speedbridge2.object;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@Data
@NoArgsConstructor(force = true)
public class Position {

    @Embedded
    private final World world;
    private final int x, y, z;

    public Position(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location substract(Location location) {
        return new Location(this.world, this.x - location.getX(), this.y - location.getY(), this.z - location.getZ(), location.getYaw(), location.getPitch());
    }
}