package io.tofpu.speedbridge2.command.parser;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.util.material.MultiMaterial;
import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import org.bukkit.Material;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.process.ValueResolver;

@AutoRegister
public class MaterialParser extends AbstractLampParser<Material> {
    public MaterialParser(LampParseRegistry registry) {
        super(Material.class, registry);
    }

    @Override
    Material parse(ValueResolver.ValueResolverContext context) {
        System.out.println("MaterialParser#parse called");
        String materialName = context.pop();
        try {
            return MultiMaterial.getOrThrow(materialName);
        } catch (Exception e) {
            throw new CommandErrorException(String.format(BridgeUtil.miniMessageToLegacy(Message.INSTANCE.invalidMaterial), materialName));
        }
    }
}
