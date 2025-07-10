package io.tofpu.speedbridge2.game.infra.config.experience.meta;

import com.cryptomorin.xseries.XSound;
import space.arim.dazzleconf.annote.ConfDefault;

import static org.immutables.value.Value.Immutable;

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

    default net.kyori.adventure.sound.Sound toSound() {
        return net.kyori.adventure.sound.Sound.sound(type().get().key(), net.kyori.adventure.sound.Sound.Source.AMBIENT, volume(), pitch());
    }

    class Builder extends ImmutableSound.Builder {}
}
