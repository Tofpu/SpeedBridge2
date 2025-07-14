package io.tofpu.speedbridge2.game.infra.config.experience.meta;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import space.arim.dazzleconf.annote.ConfDefault;

import static org.immutables.value.Value.Immutable;

// todo: add support for specifying a fade in and fade out time
@Immutable
public interface Title {
    static Builder builder() {
        return new Builder();
    }

    static Title of(String title, String subtitle) {
        return ImmutableTitle.of(title, subtitle);
    }

    @ConfDefault.DefaultString("")
    String title();

    @ConfDefault.DefaultString("")
    String subtitle();

    default net.kyori.adventure.title.Title toTitle() {
        TextComponent title = LegacyComponentSerializer.legacyAmpersand().deserialize(title());
        TextComponent subtitle = LegacyComponentSerializer.legacyAmpersand().deserialize(subtitle());
        return net.kyori.adventure.title.Title.title(title, subtitle);
    }

    class Builder extends ImmutableTitle.Builder {}
}
