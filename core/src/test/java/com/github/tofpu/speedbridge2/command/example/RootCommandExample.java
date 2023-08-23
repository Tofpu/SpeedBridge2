package com.github.tofpu.speedbridge2.command.example;

import com.github.tofpu.speedbridge2.command.annontation.Command;
import com.github.tofpu.speedbridge2.command.annontation.Default;
import com.github.tofpu.speedbridge2.command.annontation.Join;
import com.github.tofpu.speedbridge2.command.annontation.Subcommand;

import java.util.Arrays;

@Command(name = "root")
public class RootCommandExample {

    private final PrintCommandExample printCommandExample;

    public RootCommandExample(PrintCommandExample printCommandExample) {
        this.printCommandExample = printCommandExample;
    }

    public RootCommandExample() {
        this(new PrintCommandExample());
    }

//    @Default
//    public void welcome(@Join String[] arguments) {
//        if (arguments != null) {
//            System.out.println("Unknown argument: " + Arrays.toString(arguments));
//        }
//        System.out.println("Root commands:");
//        System.out.println("--/print");
//        System.out.println("--/print <input>");
//        System.out.println("--/print set <defaultMessage>");
//    }

    @Default
    public void welcome(@Join String[] arguments) {
        if (arguments != null) {
            System.out.println("Unknown argument: " + Arrays.toString(arguments));
        }
        System.out.println("Root commands:");
        System.out.println("--/print");
        System.out.println("--/print <input>");
        System.out.println("--/print set <defaultMessage>");
    }

    @Subcommand(name = "say")
    public void say(@Join Integer[] numbers) {
        System.out.println("You said " + Arrays.toString(numbers) + " numbers!");
    }

    public PrintCommandExample printCommandExample() {
        return printCommandExample;
    }
}