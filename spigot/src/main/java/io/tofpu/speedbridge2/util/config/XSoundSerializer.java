package io.tofpu.speedbridge2.util.config;

import com.cryptomorin.xseries.XSound;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

public class XSoundSerializer implements ValueSerialiser<XSound> {
    @Override
    public Class<XSound> getTargetClass() {
        return XSound.class;
    }

    @Override
    public XSound deserialise(FlexibleType flexibleType) throws BadValueException {
        String soundName = flexibleType.getString();
        return XSound.of(soundName)
                .orElseThrow(() -> new RuntimeException("Invalid XSound value: " + soundName));
    }

    @Override
    public Object serialise(XSound value, Decomposer decomposer) {
        return value.name();
    }
}
