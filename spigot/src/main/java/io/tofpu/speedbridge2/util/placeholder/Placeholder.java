package io.tofpu.speedbridge2.util.placeholder;

public class Placeholder {
    private final String match;
    private final String replacement;

    public static Placeholder of(String match, String replacement) {
        return new Placeholder(match, replacement);
    }

    public static String replaceAll(String text, Placeholder... placeholders) {
        for (Placeholder placeholder : placeholders) {
            text = placeholder.replace(text);
        }
        return text;
    }

    public Placeholder(String match, String replacement) {
        this.match = match;
        this.replacement = replacement;
    }

    public String replace(String text) {
        return text.replaceAll(match, replacement);
    }
}
