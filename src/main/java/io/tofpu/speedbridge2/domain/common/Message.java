package io.tofpu.speedbridge2.domain.common;

import io.tofpu.speedbridge2.domain.common.util.MessageUtil;

public final class Message {
    public static final String ERROR =
            "<red>" + MessageUtil.Symbols.WARNING.getSymbol() + " ";

    public static final String ISLAND_ALREADY_EXISTS =
            ERROR + "Island %s already exists!";

    public static final String ISLAND_HAS_BEEN_CREATED =
            "<green>Island %s has been " + "created!";
    public static final String ISLAND_HAS_BEEN_CREATED_SCHEMATIC =
            ISLAND_HAS_BEEN_CREATED + " with %s chosen as a schematic!";

    public static final String VALID_SELECT =
            "<green>Island %s has been selected" + " \"%s\" " + "as a " + "%s!";

    public static final String INVALID_SCHEMATIC =
            ERROR + "%s cannot be found as a schematic";
    public static final String INVALID_ISLAND = ERROR + "Island %s cannot be found!";

    public static final String ALREADY_IN_A_ISLAND =
            ERROR + "You're already on an " + "island!";

    public static final String SCORE_TITLE_BAR = MessageUtil.CHAT_BAR.substring(0,
            MessageUtil.CHAT_BAR.length() / 6);

    public static final String SCORE_TITLE =
            "<yellow>" + SCORE_TITLE_BAR + "  " + "<gold><bold" +
            "> YOUR SCORES</bold></gold>" + " " + SCORE_TITLE_BAR;

    public static final String JOINED_AN_ISLAND = "<yellow>You joined the island %s!";
    public static final String LEFT_AN_ISLAND = "<yellow>You left from island %s!";
    public static final String NOT_IN_A_ISLAND = ERROR + "You're not on an island!";

    public static final String DELETED_AN_ISLAND =
            "<gold><bold>" + MessageUtil.Symbols.CROSS.getSymbol() + "</bold> " +
            "<yellow>Island %s has been deleted!";

    public static final String EMPTY_SELECT = ERROR + "You haven't modified anything...";

    // from listeners

    public static final String STYLE =
            "<gold>" + MessageUtil.Symbols.CLOCK.getSymbol() + "<yellow> ";
    public static final String SECOND_STYLE =
            "<gold>" + MessageUtil.Symbols.STAR.getSymbol() + "<yellow> ";

    public static final String TIME_STARTED = STYLE + "The timer is now ticking!";
    public static final String SCORED =
            SECOND_STYLE + "You scored <yellow>%s</yellow> " + "seconds!";
}
