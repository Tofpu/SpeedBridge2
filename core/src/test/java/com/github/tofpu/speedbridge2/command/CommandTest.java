package com.github.tofpu.speedbridge2.command;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.github.tofpu.speedbridge2.command.example.PrintCommandExample;
import com.github.tofpu.speedbridge2.command.example.RootCommandExample;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandTest {
    private final PrintCommandExample printCommand = spy(new PrintCommandExample());
    private final RootCommandExample rootCommand = spy(new RootCommandExample(printCommand));
    private final CommandHandler commandHandler = new CommandHandler();

    @BeforeEach
    void setUp() {
        commandHandler.register(rootCommand);
    }

    @Test
    void default_invoke_test() {
        commandHandler.invoke("root");
        verify(rootCommand, times(1)).welcome(eq(null));
        verifyNoMoreInteractions(rootCommand);
        verifyNoInteractions(printCommand);

        commandHandler.invoke("root spawn daddy 123 4");
        verify(rootCommand, times(1)).welcome(eq(new String[]{"spawn", "daddy", "123", "4"}));
        verifyNoMoreInteractions(rootCommand);
        verifyNoInteractions(printCommand);

        commandHandler.invoke("root print");
        verify(printCommand, times(1)).print(eq(null));
        verifyNoMoreInteractions(printCommand);
        verifyNoMoreInteractions(rootCommand);
    }

    @Test
    void basic_test() {
        commandHandler.invoke("root print hi");
        verify(printCommand, times(1)).print(anyString());
        verifyNoMoreInteractions(printCommand);
        verifyNoMoreInteractions(rootCommand);

        commandHandler.invoke("root print test message");
        verify(printCommand, times(1)).testHi();
        verifyNoMoreInteractions(printCommand);
        verifyNoMoreInteractions(rootCommand);
    }

    @Test
    void arguments_test() {
        commandHandler.invoke("root print set hello");

        verify(printCommand, times(1)).setDefaultPrintMessage(eq("hello"));
        verifyNoMoreInteractions(printCommand);
        verifyNoMoreInteractions(rootCommand);
    }

    @Test
    void argument_resolver_test() {
        // test integer arrays
        commandHandler.invoke("root say 1 2 3");
        verify(rootCommand, times(1)).say(eq(new Integer[]{1, 2, 3}));
        verifyNoMoreInteractions(rootCommand);
        verifyNoInteractions(printCommand);
    }
}
