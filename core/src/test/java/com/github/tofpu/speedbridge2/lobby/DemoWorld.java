package com.github.tofpu.speedbridge2.lobby;

import com.github.tofpu.speedbridge2.object.generic.World;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(force = true)
public class DemoWorld extends World {

    public DemoWorld(String worldName) {
        super(worldName);
    }
}