package com.github.tofpu.speedbridge2.common.lobby;

import com.github.tofpu.speedbridge2.object.World;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(force = true)
public class DemoWorld extends World {

    public DemoWorld(String worldName) {
        super(worldName);
    }
}