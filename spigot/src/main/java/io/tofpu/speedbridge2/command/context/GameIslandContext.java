package io.tofpu.speedbridge2.command.context;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.object.GameIsland;
import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.process.ContextResolver;

import java.lang.reflect.Parameter;
import java.util.Arrays;

import static io.tofpu.speedbridge2.model.common.Message.INSTANCE;

@AutoRegister
public final class GameIslandContext extends AbstractLampContext<GameIsland> {
    private final PlayerService playerService;

    public GameIslandContext(final LampContextRegistry registry, final PlayerService playerService) {
        super(GameIsland.class, registry);
        this.playerService = playerService;
    }

    @Override
    public GameIsland resolve(final ContextResolver.ContextResolverContext context) {
        final BridgePlayer player = playerService.getIfPresent(context.actor().getUniqueId());
        Parameter javaParameter = context.parameter().getJavaParameter();
        boolean canBeNull = javaParameter.isAnnotationPresent(Nullable.class) || context.parameter().isOptional();
        if (!canBeNull) {
            System.out.println("list of annotations: " + Arrays.toString(javaParameter.getAnnotations()));
            System.out.println("list of annotations types: " + javaParameter.getAnnotatedType());
            System.out.println("list of declared annotations: " + Arrays.toString(javaParameter.getDeclaredAnnotations()));
            System.out.println("alternative: " + context.command().hasAnnotation(Nullable.class));
            System.out.println("alternative: " + context.command().getParameters().stream().anyMatch(commandParameter -> {
                boolean result = commandParameter.hasAnnotation(Nullable.class);
                if (result) {
                    System.out.println(commandParameter + " has nullable annotation");
                }
                return result;
            }));
        }
//        context.parameter().getJavaParameter().
        System.out.println(String.format("GameIslandContext: %s can be null? %s", context.actor().getName(), canBeNull));

        if (player == null && canBeNull) {
            return null;
        } else if (player == null) {
            throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(INSTANCE.notLoaded));
        }

        final GameIsland currentGame = player.getCurrentGame();
        if (currentGame == null && canBeNull) {
            return null;
        } else if (currentGame == null) {
            throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(INSTANCE.notInAIsland));
        }
        return currentGame;
    }
}
