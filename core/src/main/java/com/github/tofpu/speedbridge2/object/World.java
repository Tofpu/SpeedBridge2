package com.github.tofpu.speedbridge2.object;

import javax.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(force = true)
@Data
public class World {

    private final String worldName;

    public World(String worldName) {
        this.worldName = worldName;
    }
}