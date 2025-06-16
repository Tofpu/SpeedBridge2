package io.tofpu.speedbridge2.game.infra.config.experience.meta;

import static org.immutables.value.Value.Immutable;

import com.cryptomorin.xseries.XSound;
import org.bukkit.entity.Player;
import space.arim.dazzleconf.annote.ConfDefault;

@Immutable
public interface Sound {
    static Builder builder() {
        return new Builder();
    }

    static Sound of(XSound type, float volume, float pitch) {
        return ImmutableSound.of(type, volume, pitch);
    }

    @ConfDefault.DefaultString("UI_BUTTON_CLICK")
    XSound type();

    @ConfDefault.DefaultDouble(1.0f)
    float volume();

    @ConfDefault.DefaultDouble(1.0f)
    float pitch();

    default void play(Player player) {
        type().play(player, volume(), pitch());
    }

    class Builder extends ImmutableSound.Builder {}
}
