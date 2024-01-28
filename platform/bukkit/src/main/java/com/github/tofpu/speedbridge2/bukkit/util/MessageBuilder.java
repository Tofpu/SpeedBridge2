package com.github.tofpu.speedbridge2.bukkit.util;

import java.util.StringJoiner;

public class MessageBuilder {
    private final StringBuilder builder;

    public MessageBuilder(String content) {
        this.builder = new StringBuilder(content);
    }

    public MessageBuilder() {
        this.builder = new StringBuilder();
    }

    public MessageBuilder append(String content) {
        this.builder.append(ChatUtil.colorize(content));
        return this;
    }

    public MessageBuilder appendNewLine() {
        append("&r \n");
        return this;
    }

    @Override
    public String toString() {
        return this.builder.toString();
    }

    public MessageBuilder appendSpace() {
        append(" ");
        return this;
    }
}
