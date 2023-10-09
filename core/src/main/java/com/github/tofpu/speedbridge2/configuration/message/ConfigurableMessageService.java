package com.github.tofpu.speedbridge2.configuration.message;

import com.github.tofpu.speedbridge2.MagicValue;
import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;
import com.github.tofpu.speedbridge2.configuration.service.PluginConfigTypes;
import com.github.tofpu.speedbridge2.object.player.ConfigurableMessage;
import com.github.tofpu.speedbridge2.service.LoadableService;
import com.github.tofpu.speedbridge2.util.ReflectionUtil;
import io.github.tofpu.dynamicconfiguration.Configuration;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigurableMessageService implements LoadableService {
    private final ConfigurationService configurationService;

    public ConfigurableMessageService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public void load() {
        readMessages();
    }

    private void readMessages() {
        Configuration configuration = configurationService.on(PluginConfigTypes.MESSAGE);
        AtomicBoolean saveChanges = new AtomicBoolean(false);
        configurableMessages().forEach((field, configurableMessage) -> {
            System.out.printf("Found %s field with key %s and %s message%n", field.getName(), configurableMessage.key(), configurableMessage.defaultMessage());

            String newMessage = configuration.getString(configurableMessage.key(), null);
            if (newMessage == null) {
                System.out.printf("Set %s=%s%n", configurableMessage.key(), configurableMessage.defaultMessage());
                configuration.set(configurableMessage.key(), configurableMessage.defaultMessage());
            } else {
                System.out.println("Located message from file=" + newMessage);
                saveChanges.set(true);
                configurableMessage.setMessage(newMessage);
            }
        });

        if (saveChanges.get()) {
            configurationService.save(PluginConfigTypes.MESSAGE);
        }
    }

    private Map<Field, ConfigurableMessage> configurableMessages() {
        final Map<Field, ConfigurableMessage> map = new HashMap<>();

        Set<Class<? extends ConfigurableMessage>> configurableMessageClasses = new Reflections(MagicValue.APPLICATION_PACKAGE_NAME).getSubTypesOf(ConfigurableMessage.class);
        configurableMessageClasses.forEach(aClass -> {
            if (!Enum.class.isAssignableFrom(aClass)) return;
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                ConfigurableMessage value;
                try {
                    value = (ConfigurableMessage) ReflectionUtil.get(declaredField, null);
                } catch (Exception e) {
                    continue;
                }
                map.put(declaredField, value);
            }
        });
        return map;
    }

    @Override
    public void unload() {

    }

    public void reload() {
        configurationService.reload(PluginConfigTypes.MESSAGE);
        readMessages();
    }
}
