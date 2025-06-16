package io.tofpu.speedbridge2.game.infra.config.experience.meta;

import static org.immutables.value.Value.Immutable;

import org.bukkit.entity.Player;
import space.arim.dazzleconf.annote.ConfDefault;

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

    default void show(Player player) {
        player.sendTitle(title(), subtitle());
    }

    class Builder extends ImmutableTitle.Builder {}
}
