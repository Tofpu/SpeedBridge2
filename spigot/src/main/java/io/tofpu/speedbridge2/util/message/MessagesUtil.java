package io.tofpu.speedbridge2.util.message;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MessagesUtil {

    public static String format(String component, String key, Object... args) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages/" + component);
        String pattern;
        try {
            pattern = bundle.getString(key);
        } catch (Exception e) {
            // If the key is not found, return the key itself as a fallback
            pattern = key;
        }
        return MessageFormat.format(pattern, args);
    }
}
