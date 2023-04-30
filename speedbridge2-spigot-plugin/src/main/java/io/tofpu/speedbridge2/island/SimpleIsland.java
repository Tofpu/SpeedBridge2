package io.tofpu.speedbridge2.island;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "islands")
public class SimpleIsland {
    @Id
    private final UUID id;
    private final int slot;
    private final String category;
    private final IslandSchematicInfo islandSchematicInfo;

    public SimpleIsland() {
        this(-1, "", null, null);
    }

    public SimpleIsland(int slot, String category, String schematicName, SchematicLocation schematicLocation) {
        this.id = UUID.randomUUID();
        this.slot = slot;
        this.category = category;
        this.islandSchematicInfo = new IslandSchematicInfo(id, schematicName, schematicLocation);
    }

    public UUID getId() {
        return id;
    }

    public int getSlot() {
        return slot;
    }

    public String getCategory() {
        return category;
    }

    public IslandSchematicInfo getIslandSchematicInfo() {
        return islandSchematicInfo;
    }
}
