package io.tofpu.speedbridge2.game.infra.config.experience.meta;

import space.arim.dazzleconf.annote.ConfDefault;

import static net.kyori.adventure.text.Component.text;
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
        return net.kyori.adventure.title.Title.title(text(title()), text(subtitle()));
    }

    class Builder extends ImmutableTitle.Builder {}
}
