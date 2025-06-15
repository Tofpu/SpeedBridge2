package io.tofpu.speedbridge2.game.config.item.serializer;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

public class ItemMetaOptionsSerializer implements ValueSerialiser<ItemMetaOptions> {

    protected static final String DISPLAY_NAME = "displayName";
    protected static final String LORE = "lore";

    @Override
    public Class<ItemMetaOptions> getTargetClass() {
        return ItemMetaOptions.class;
    }

    @Override
    public ItemMetaOptions deserialise(FlexibleType flexibleType) throws BadValueException {
        Map<String, Object> map = flexibleType.getMap((flexibleKey, flexibleValue) ->
                new AbstractMap.SimpleEntry<>(flexibleKey.getString(), flexibleKey.getObject(Object.class)));
        String displayName = "";
        if (map.containsKey(DISPLAY_NAME)) {
            displayName = map.get(DISPLAY_NAME).toString();
        }
        List<String> lore = Collections.emptyList();
        if (map.containsKey(LORE)) {
            lore = (List<String>) map.get(LORE);
        }
        return new ItemMetaOptions(displayName, lore);
    }

    @Override
    public Object serialise(ItemMetaOptions value, Decomposer decomposer) {
        Map<String, Object> map = new HashMap<>();
        if (!value.displayName().isBlank()) {
            map.put(DISPLAY_NAME, value.displayName());
        }
        List<String> lore = value.lore();
        if (lore != null) {
            map.put(LORE, decomposer.decomposeCollection(String.class, lore));
        }
        return map;
    }
}
