package io.tofpu.speedbridge2.model.common.config.category;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public final class GeneralCategory {
    @Setting("show-debug-message")
    @Comment("Useful when debugging")
    private boolean showDebugMessage = false;
    @Setting("default-island-category")
    @Comment("This will set the default island category when they're not provided upon " +
            "creation")
    private String defaultIslandCategory = "standard";
    @Setting("island-space-gap")
    @Comment("The space gap between islands")
    private int islandSpaceGap = 10;

    public boolean isDebugEnabled() {
        return showDebugMessage;
    }

    public String getDefaultIslandCategory() {
        return defaultIslandCategory;
    }

    public int getIslandSpaceGap() {
        return islandSpaceGap;
    }
}
