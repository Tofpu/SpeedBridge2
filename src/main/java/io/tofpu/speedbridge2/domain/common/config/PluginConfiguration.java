package io.tofpu.speedbridge2.domain.common.config;

import io.tofpu.speedbridge2.domain.common.config.category.GeneralCategory;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public final class PluginConfiguration {
    @Setting
    private final GeneralCategory generalCategory = new GeneralCategory();

    public GeneralCategory getGeneralCategory() {
        return generalCategory;
    }
}
