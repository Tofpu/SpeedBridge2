package io.tofpu.speedbridge2.util.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class EasyMessageBuilder {
    public static EasyMessageBuilder create() {
        return new EasyMessageBuilder();
    }

    private Component component = Component.empty();

    public EasyMessageBuilder addComponent(Component component) {
        this.component = this.component.append(component);
        return this;
    }

    public EasyMessageBuilder addText(String text, NamedTextColor color, TextDecoration ...textDecorations) {
        return addComponent(Component.text(text, color, textDecorations));
    }

    public EasyMessageBuilder addNewLine() {
        return addComponent(Component.newline());
    }

    public EasyMessageBuilder addEmptySpace() {
        return addComponent(Component.space());
    }

    public EasyMessageBuilder addReplacement(String placeholder, String replacement, NamedTextColor color) {
        return addReplacement(placeholder, Component.text(replacement, color));
    }

    public EasyMessageBuilder addReplacement(String placeholder, String replacement) {
        return addReplacement(placeholder, Component.text(replacement));
    }

    public EasyMessageBuilder addReplacement(String placeholder, Component replacement) {
        this.component = this.component.replaceText(builder -> builder.matchLiteral(placeholder).replacement(replacement));
        return this;
    }

    public Component build() {
        return component;
    }
}
