package io.tofpu.speedbridge2.game.config;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import io.tofpu.speedbridge2.game.config.experience.GamePlayerExperienceConfiguration;
import io.tofpu.speedbridge2.game.config.item.GameItemConfiguration;
import io.tofpu.speedbridge2.util.ColorUtil;
import io.tofpu.speedbridge2.util.ItemStackBuilder;

/**
 * This class is responsible for housing the default configuration values for {@link GameConfiguration}.
 */
public interface GameConfigDefaults {
    interface Experience {
        static GamePlayerExperienceConfiguration.GameOptions resetOptions() {
            return GamePlayerExperienceConfiguration.GameOptions.builder()
                    .sound(XSound.UI_BUTTON_CLICK, 1, 1)
                    .title("Game Reset", "Game has been reset")
                    .addMessages("Game has been reset")
                    .build();
        }

        static GamePlayerExperienceConfiguration.GameOptions scoreOptions() {
            return GamePlayerExperienceConfiguration.GameOptions.builder()
                    .sound(XSound.UI_BUTTON_CLICK, 1, 1)
                    .title("Game Scored", "Game has been scored")
                    .addMessages("It took %time% to reach the end.")
                    .build();
        }

        static GamePlayerExperienceConfiguration.GameOptions beatenScoreOptions() {
            return GamePlayerExperienceConfiguration.GameOptions.builder()
                    .sound(XSound.UI_BUTTON_CLICK, 1, 1)
                    .title("Game Beaten", "Game has been beaten")
                    .addMessages(
                            "You broke your personal best score of %time%",
                            "Your new personal best score is %time%")
                    .build();
        }
    }

    interface Items {
        static GameItemConfiguration.Item leaveGameItem() {
            return GameItemConfiguration.Item.of(
                    ItemStackBuilder.newBuilder()
                            .displayName(ColorUtil.colorize("&eLeave"))
                            .lore(ColorUtil.colorize("&7Click to leave the game"))
                            .apply(XMaterial.RED_BED.parseItem()),
                    8);
        }
        static GameItemConfiguration.Item resetGameItem() {
            return GameItemConfiguration.Item.of(
                    ItemStackBuilder.newBuilder()
                            .displayName(ColorUtil.colorize("&cReset"))
                            .lore(ColorUtil.colorize("&7Click to reset the game"))
                            .apply(XMaterial.RED_DYE.parseItem()),
                    7);
        }
    }
}
