package io.tofpu.speedbridge2.game.infra.config;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import io.tofpu.speedbridge2.game.infra.config.experience.GamePlayerExperienceConfiguration;
import io.tofpu.speedbridge2.game.infra.config.item.GameHotbarConfiguration;
import io.tofpu.speedbridge2.util.ColorUtil;
import io.tofpu.speedbridge2.util.ItemStackBuilder;

/**
 * This class is responsible for housing the default configuration values for {@link GameConfiguration}.
 */
public interface GameConfigDefaults {
    interface Experience {
        static GamePlayerExperienceConfiguration.GameOptions resetOptions() {
            return GamePlayerExperienceConfiguration.GameOptions.builder()
                    .sound(XSound.BLOCK_NOTE_BLOCK_HAT, 1.2f, 1.8f) // Snappy, quick sound
                    .title("&cReset!", "&7You’ve been reset. Get ready!")
                    .addMessages(
                            "&7Bridge failed! Quick fingers—try again!",
                            "&8⌛ Time reset. Stay sharp!")
                    .build();
        }

        static GamePlayerExperienceConfiguration.GameOptions scoreOptions() {
            return GamePlayerExperienceConfiguration.GameOptions.builder()
                    .sound(XSound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.6f) // Quick feedback
                    .title("&aSuccess!", "&7Bridge completed.")
                    .addMessages(
                            "&aYou finished the bridge in &e%time%&a!",
                            "&7Clean run—how fast can you go next?")
                    .build();
        }

        static GamePlayerExperienceConfiguration.GameOptions beatenScoreOptions() {
            return GamePlayerExperienceConfiguration.GameOptions.builder()
                    .sound(XSound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.2f) // Satisfying success tone
                    .title("&6New Record!", "&eNew personal best!")
                    .addMessages(
                            "&eYou beat your time of &c%previous_time%&e!",
                            "&aNew PB: &b%time%",
                            "&7⚡ Think you can go even faster?")
                    .build();
        }
    }

    interface Items {
        static GameHotbarConfiguration.Item leaveGameItem() {
            return GameHotbarConfiguration.Item.of(
                    ItemStackBuilder.newBuilder()
                            .displayName(ColorUtil.colorize("&eLeave"))
                            .lore(ColorUtil.colorize("&7Click to leave the game"))
                            .apply(XMaterial.RED_BED.parseItem()),
                    8);
        }
        static GameHotbarConfiguration.Item resetGameItem() {
            return GameHotbarConfiguration.Item.of(
                    ItemStackBuilder.newBuilder()
                            .displayName(ColorUtil.colorize("&cReset"))
                            .lore(ColorUtil.colorize("&7Click to reset the game"))
                            .apply(XMaterial.RED_DYE.parseItem()),
                    7);
        }
    }
}
