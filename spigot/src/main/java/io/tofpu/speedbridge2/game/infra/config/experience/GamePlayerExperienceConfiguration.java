package io.tofpu.speedbridge2.game.infra.config.experience;

import io.tofpu.speedbridge2.game.infra.config.experience.meta.Sound;
import io.tofpu.speedbridge2.game.infra.config.experience.meta.Title;
import io.tofpu.speedbridge2.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.SubSection;

import java.util.List;
import java.util.Map;

import static io.tofpu.speedbridge2.game.infra.config.GameConfigDefaults.Experience.*;
import static org.immutables.value.Value.Immutable;

/**
 * This class is responsible for customizing the player experience. Like, when the game is reset, scored, or a new personal best score is achieved.
 */
@Immutable
public interface GamePlayerExperienceConfiguration {
    static ImmutableGamePlayerExperienceConfiguration.Builder builder() {
        return ImmutableGamePlayerExperienceConfiguration.builder();
    }

    enum Type {
        RESET,
        SCORE,
        BEATEN_SCORE
    }

    @ConfDefault.DefaultObject("defaultOptions")
    Map<Type, @SubSection GameOptions> options();

    static Map<Type, GameOptions> defaultOptions() {
        return Map.of(
                Type.RESET, resetOptions(),
                Type.SCORE, scoreOptions(),
                Type.BEATEN_SCORE, beatenScoreOptions()
        );
    }

    default GameOptions reset() {
        return options().get(Type.RESET);
    }

    default GameOptions score() {
        return options().get(Type.SCORE);
    }

    default GameOptions beatenScore() {
        return options().get(Type.BEATEN_SCORE);
    }

    @Immutable
    interface GameOptions {

        static ImmutableGameOptions.Builder builder() {
            return ImmutableGameOptions.builder();
        }

        @SubSection
        Sound sound();

        @SubSection
        Title title();

        @ConfDefault.DefaultStrings({})
        List<String> commands();

        @ConfDefault.DefaultStrings({})
        List<String> messages();

        default void apply(Player player) {
            sound().play(player);
            title().show(player);
            sendMessages(player);
            performCommands(player);
        }

        default void sendMessages(Player player) {
            // apply placeholders to the commands before sending
            for (String message : messages()) {
                player.sendMessage(ColorUtil.colorize(message));
            }
        }

        default void performCommands(Player player) {
            // apply placeholders to the commands before dispatching
            for (String command : commands()) {
                Server server = Bukkit.getServer();
                server.dispatchCommand(server.getConsoleSender(), command);
            }
        }
    }
}
