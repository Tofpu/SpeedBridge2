package io.tofpu.speedbridge2.domain.common;

import io.tofpu.speedbridge2.domain.common.util.IgnoreMessage;
import io.tofpu.speedbridge2.domain.common.util.MessageUtil;
import io.tofpu.speedbridge2.domain.common.util.ReflectionUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

// the variables are not final due to modern versions of Java not allowing
// static final's to be modified, and therefore, I'm breaking the conventions! sorry!
public final class Message {
    private static final Map<String, Field> FIELD_MAP = new ConcurrentHashMap<>();

    @IgnoreMessage
    public static String ERROR = "<red>" + MessageUtil.Symbols.WARNING.getSymbol() + " ";
    @IgnoreMessage
    public static String SUCCESS =
            "<gold><bold>" + MessageUtil.Symbols.CROSS.getSymbol() + "</bold> <yellow>";

    public static String ISLAND_ALREADY_EXISTS = ERROR + "Island %s already exists!";

    public static String ISLAND_HAS_BEEN_CREATED =
            SUCCESS + "Island %s has been " + "created!";
    public static String ISLAND_HAS_BEEN_CREATED_SCHEMATIC =
            ISLAND_HAS_BEEN_CREATED + " with %s chosen as a schematic!";

    public static String VALID_SELECT =
            SUCCESS + "Island %s has selected" + " \"%s\" " + "as a " + "%s!";

    public static String INVALID_SCHEMATIC =
            ERROR + "\"%s\" cannot be found as a schematic";

    public static String INVALID_ISLAND_ARGUMENT =
            ERROR + "You have to insert a " + "slot or a category" +
            ". or you could run the /randomjoin command.";
    public static String INVALID_ISLAND = ERROR + "Island %s cannot be found!";
    public static String NO_AVAILABLE_ISLAND = ERROR + "There is no island available " +
                                               "at the moment... please try again " +
                                               "later!";

    public static String ALREADY_IN_A_ISLAND =
            ERROR + "You're already on an " + "island!";

    @IgnoreMessage
    public static String SCORE_TITLE_BAR = MessageUtil.CHAT_BAR.substring(0,
            MessageUtil.CHAT_BAR.length() / 6);

    public static String SCORE_TITLE =
            "<yellow>" + SCORE_TITLE_BAR + "  " + "<gold><bold" +
            "> YOUR SCORES</bold></gold>" + " " + SCORE_TITLE_BAR;

    public static String JOINED_AN_ISLAND = SUCCESS + "You joined the " + "island %s!";
    public static String LEFT_AN_ISLAND = SUCCESS + "You left from island" + " %s!";
    public static String NOT_IN_A_ISLAND = ERROR + "You're not on an island!";

    public static String DELETED_AN_ISLAND = SUCCESS + "Island %s has been deleted!";

    public static String EMPTY_SELECT = ERROR + "You haven't modified anything...";

    public static String RELOADED = SUCCESS + "The config has been reloaded!";

    // from listeners

    @IgnoreMessage
    public static String STYLE =
            "<gold>" + MessageUtil.Symbols.CLOCK.getSymbol() + "<yellow> ";
    @IgnoreMessage
    public static String SECOND_STYLE =
            "<gold>" + MessageUtil.Symbols.STAR.getSymbol() + "<yellow> ";

    public static String TIME_STARTED = STYLE + "The timer is now ticking!";
    public static String SCORED =
            SECOND_STYLE + "You scored <yellow>%s</yellow> " + "seconds!";

    static {
        CompletableFuture.runAsync(() -> {
            for (final Field field : Message.class.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) || field.isAnnotationPresent(IgnoreMessage.class)) {
                    continue;
                }

                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                FIELD_MAP.put(field.getName(), field);
            }
        });
    }

    public static CompletableFuture<Void> load(final File directory) {
        final File messages = new File(directory, "messages.yml");
        final boolean exist = messages.exists();

        final Class<Message> messageClass = Message.class;
        if (!exist) {
            try (final Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(messages), StandardCharsets.UTF_8));) {
                for (final String message : ReflectionUtil.toString(FIELD_MAP,
                        messageClass)) {
                    writer.write(message);
                    writer.write("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.runAsync(() -> {
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(messages), "UTF8"))) {
                while (reader.ready()) {
                    final String input = reader.readLine();
                    final String[] args = input.split(":");

                    final String fieldName = args[0];
                    final String message = args[1].replaceFirst(" ", "");

                    final Field field = FIELD_MAP.get(fieldName);
                    if (field == null) {
                        continue;
                    }

                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    field.set(null, message);
                }
            } catch (IOException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        });
    }
}
