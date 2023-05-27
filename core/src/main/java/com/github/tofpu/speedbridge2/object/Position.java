package com.github.tofpu.speedbridge2.object;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}