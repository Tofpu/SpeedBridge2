package com.github.tofpu.speedbridge2.command.example;

import com.github.tofpu.speedbridge2.command.Command;
import com.github.tofpu.speedbridge2.command.Default;

@Command(name = "root")
public class RootCommandExample {
    private final PrintCommandExample printCommandExample;

    public RootCommandExample(PrintCommandExample printCommandExample) {
        this.printCommandExample = printCommandExample;
    }

    public RootCommandExample() {
        this(new PrintCommandExample());
    }

    @Default
    public void welcome() {
        System.out.println("Root commands:");
        System.out.println("--/print");
        System.out.println("--/print <input>");
        System.out.println("--/print set <defaultMessage>");
    }

    public PrintCommandExample printCommandExample() {
        return printCommandExample;
    }
}
