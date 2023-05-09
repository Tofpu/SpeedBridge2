package io.tofpu.speedbridge2.game.schematic;

import io.tofpu.speedbridge2.game.generic.BlockType;

import java.util.Objects;
import java.util.StringJoiner;

public class DummyBlockType extends BlockType {
    private final String materialType;

    public DummyBlockType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialType() {
        return materialType;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DummyBlockType.class.getSimpleName() + "[", "]")
                .add("materialType='" + materialType + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DummyBlockType that = (DummyBlockType) o;

        return Objects.equals(materialType, that.materialType);
    }

    @Override
    public int hashCode() {
        return materialType != null ? materialType.hashCode() : 0;
    }
}
