package io.tofpu.speedbridge2.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;

import java.util.Collections;

// yoinked from LearnSpigot, thanks <3
public final class MessageUtil {

    /**
     * Good sized bar for scoreboards
     */
    public static final String SB_BAR = ChatColor.STRIKETHROUGH + "----------------------";
    /**
     * Good sized bar for guis
     */
    public static final String MENU_BAR = ChatColor.STRIKETHROUGH + "------------------------";
    /**
     * Good sized bar for information messages
     */
    public static final String CHAT_BAR = ChatColor.STRIKETHROUGH + "------------------------------------------------";
    /**
     * No text in the message, adds a bit of space between a message
     */
    public static final String BLANK_MESSAGE = String.join("", Collections.nCopies(150, " \n"));

    /**
     * A useful enum with a bunch of symbols you may want to access
     */
    public enum Symbols {
        /**
         * Returns "❤" symbol
         */
        HEALTH(StringEscapeUtils.unescapeJava("\u2764")),
        /**
         * Returns "★" symbol
         */
        STAR(StringEscapeUtils.unescapeJava("\u2605")),
        /**
         * Returns "⟳" symbol
         */
        CLOCK(StringEscapeUtils.unescapeJava("\u27F3")),
        /**
         * Returns "«" symbol
         */
        ARROW_LEFT(StringEscapeUtils.unescapeJava("\u00AB")),
        /**
         * Returns "»" symbol
         */
        ARROW_RIGHT(StringEscapeUtils.unescapeJava("\u00BB")),
        /**
         * Returns "✖" symbol
         */
        CROSS(StringEscapeUtils.unescapeJava("\u2716")),
        /**
         * Returns "⚠" symbol
         */
        WARNING(StringEscapeUtils.unescapeJava("\u26A0"));

        private final String symbol;

        /**
         * The constructor for the symbol enum
         *
         * @param symbol the symbol unicode
         */
        Symbols(String symbol) {
            this.symbol = symbol;
        }

        /**
         * A method to get the unicode symbol
         *
         * @return the symbol
         */
        public String getSymbol() {
            return symbol;
        }
    }
}
