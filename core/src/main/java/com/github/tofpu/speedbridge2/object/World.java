package com.github.tofpu.speedbridge2.object;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(force = true)
@Data
public class World {

    private final String worldName;

    public World(String worldName) {
        this.worldName = worldName;
    }
}