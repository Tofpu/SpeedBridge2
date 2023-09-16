package com.github.tofpu.speedbridge2.game.island;

import com.github.tofpu.speedbridge2.object.Location;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.File;

@Entity
@Data
@NoArgsConstructor(force = true)
public class Island {
    @Id
    private final int slot;
    private final IslandSchematic schematic;

    public Island(int slot, IslandSchematic schematic) {
        this.slot = slot;
        this.schematic = schematic;
    }

    @Embeddable
    @Data
    @NoArgsConstructor(force = true)
    public static class IslandSchematic {
        private final Location absolute;
        private final File schematicFile;

        public IslandSchematic(Location absolute, File schematicFile) {
            this.absolute = absolute;
            this.schematicFile = schematicFile;
        }
    }
}
