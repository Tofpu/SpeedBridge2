package com.github.tofpu.speedbridge2.island;

import com.github.tofpu.speedbridge2.object.Location;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@EqualsAndHashCode
@ToString
public class Island {
    @Id
    private final int slot;
    private final IslandSchematicData schematicData;

    public Island() {
        this.slot = -1;
        this.schematicData = new IslandSchematicData();
    }

    public Island(int slot, Location absolute, String schematicName) {
        this.slot = slot;
        this.schematicData = new IslandSchematicData(absolute, schematicName);
    }

    public int getSlot() {
        return slot;
    }

    public Location getAbsolute() {
        return schematicData.absolute;
    }

    public String getSchematicName() {
        return schematicData.schematicName;
    }

    @Embeddable
    @NoArgsConstructor(force = true)
    @EqualsAndHashCode
    @ToString
    static class IslandSchematicData {
        private final Location absolute;
        private final String schematicName;

        public IslandSchematicData(Location absolute, String schematicName) {
            this.absolute = absolute;
            this.schematicName = schematicName;
        }
    }
}
