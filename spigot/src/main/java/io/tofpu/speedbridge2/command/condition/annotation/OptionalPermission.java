package io.tofpu.speedbridge2.command.condition.annotation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.dynamic.AnnotationReplacer;
import revxrsal.commands.annotation.dynamic.Annotations;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.command.CommandPermission;
import revxrsal.commands.command.trait.CommandAnnotationHolder;
import revxrsal.commands.process.PermissionReader;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

import static revxrsal.commands.util.Collections.linkedListOf;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalPermission {
    String value();

    enum AnnotationReplacerImpl implements AnnotationReplacer<OptionalPermission> {
        INSTANCE;

        @Override
        public Collection<Annotation> replaceAnnotations(@NotNull AnnotatedElement element, @NotNull OptionalPermission annotation) {
            Optional optional = Annotations.create(Optional.class);
            return linkedListOf(optional, annotation);
        }
    }

    enum PermissionReaderImpl implements PermissionReader {
        INSTANCE {
            @Override
            public @Nullable CommandPermission getPermission(@NotNull CommandAnnotationHolder command) {
                OptionalPermission annotation = command.getAnnotation(OptionalPermission.class);
                if (annotation == null) return null;
                return actor -> {
                    BukkitCommandActor playerActor = (BukkitCommandActor) actor;
                    return playerActor.requirePlayer().hasPermission(annotation.value());
                };
            }
        }
    }
}
