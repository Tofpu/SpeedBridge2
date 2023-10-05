package com.github.tofpu.speedbridge2.lobby;

import com.github.tofpu.speedbridge2.object.World;
import javax.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(force = true)
public class DemoWorld extends World {

    public DemoWorld(String worldName) {
        super(worldName);
    }
}