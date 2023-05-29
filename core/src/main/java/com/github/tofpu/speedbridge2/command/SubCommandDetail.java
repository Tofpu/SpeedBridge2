package com.github.tofpu.speedbridge2.command;

public interface SubCommandDetail {
    String name();
    void execute(Object... args);
}
