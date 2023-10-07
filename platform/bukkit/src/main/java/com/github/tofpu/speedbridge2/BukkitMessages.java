package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.object.player.ConfigurableMessage;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public enum BukkitMessages implements ConfigurableMessage {
    PERSONAL_BEST {
        @Override
        public String key() {
            return "personal_best";
        }

        @Override
        public String defaultMessage() {
            return ChatColor.YELLOW + "You scored a personal best score of %s on %s island!".replace("%s", BukkitMessages.wrapValue());
        }
    },
    NO_PERSONAL_BEST {
        @Override
        public String key() {
            return "no_personal_best";
        }

        @Override
        public String defaultMessage() {
            return ChatColor.RED + "You have not scored anything yet.";
        }
    };

    @NotNull
    private static String wrapValue() {
        return ChatColor.WHITE + "%s" + ChatColor.YELLOW;
    }
}