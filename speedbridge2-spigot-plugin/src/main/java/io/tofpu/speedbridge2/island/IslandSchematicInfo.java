package io.tofpu.speedbridge2.island;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "island_schematic")
public class IslandSchematicInfo {
    @EmbeddedId
    private final UUID id;
    private final String schematicName;
    @Column(name = "absolute_location")
    private final SchematicLocation absoluteLocation;

    public IslandSchematicInfo(UUID id, String schematicName, SchematicLocation absoluteLocation) {
        this.id = id;
        this.schematicName = schematicName;
        this.absoluteLocation = absoluteLocation;
    }

    public UUID getId() {
        return id;
    }

    public String getSchematicName() {
        return schematicName;
    }

    public SchematicLocation getAbsoluteLocation() {
        return absoluteLocation;
    }
}
