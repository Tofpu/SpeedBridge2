package io.tofpu.speedbridge2.game.infra.config.item.serializer;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

public class ItemStackSerializer implements ValueSerialiser<ItemStack> {

    protected static final String MATERIAL = "material";
    protected static final String AMOUNT = "amount";
    protected static final String DURABILITY = "durability";
    protected static final String META = "meta";

    @Override
    public Class<ItemStack> getTargetClass() {
        return ItemStack.class;
    }

    @Override
    public ItemStack deserialise(FlexibleType flexibleType) throws BadValueException {
        Map<String, FlexibleType> map = flexibleType.getMap((flexibleKey, flexibleValue) ->
                new AbstractMap.SimpleEntry<>(flexibleKey.getString(), flexibleValue));
        String serializedMaterialName = map.get(MATERIAL).getString();
        Material material;
        try {
            material = XMaterial.matchXMaterial(serializedMaterialName).orElseThrow().get();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid material " + serializedMaterialName, e);
        }
        int amount = 1;
        if (map.containsKey(AMOUNT)) {
            amount = map.get(AMOUNT).getInteger();
        }
        short durability = 0;
        if (map.containsKey(DURABILITY)) {
            durability = map.get(DURABILITY).getShort();
        }
        ItemStack itemStack = new ItemStack(material, amount, durability);
        if (map.containsKey(META)) {
            ItemMetaOptions itemMetaOptions = map.get(META).getObject(ItemMetaOptions.class);
            itemStack = itemMetaOptions.apply(itemStack);
        }
        return itemStack;
    }

    @Override
    public Object serialise(ItemStack value, Decomposer decomposer) {
        Map<String, Object> map = new HashMap<>();
        map.put(MATERIAL, value.getType().name());
        if (value.getAmount() != 1) {
            map.put(AMOUNT, value.getAmount());
        }
        if (value.getDurability() != 0) {
            map.put(DURABILITY, value.getDurability());
        }
        if (value.hasItemMeta()) {
            map.put(META, decomposer.decompose(ItemMetaOptions.class, new ItemMetaOptions(value.getItemMeta())));
        }
        return map;
    }
}
