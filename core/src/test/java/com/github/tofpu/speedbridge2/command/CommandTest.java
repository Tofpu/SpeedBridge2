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
import com.github.tofpu.speedbridge2.command.handler.CommandHandler;
import org.junit.jupiter.api.Test;

public class CommandTest {
    @Test
    void default_invoke_test() {
        PrintCommandExample printCommand = spy(new PrintCommandExample());
        RootCommandExample rootCommand = spy(new RootCommandExample(printCommand));
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.register(rootCommand);

        commandHandler.invoke("root");
        verify(rootCommand, times(1)).welcome();
        verifyNoMoreInteractions(rootCommand);
        verifyNoInteractions(printCommand);

        commandHandler.invoke("root print");
        verify(printCommand, times(1)).print(eq(null));
        verifyNoMoreInteractions(printCommand);
        verifyNoMoreInteractions(rootCommand);
    }

    @Test
    void basic_test() {
        PrintCommandExample printCommand = spy(new PrintCommandExample());
        RootCommandExample rootCommand = spy(new RootCommandExample(printCommand));

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.register(rootCommand);

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
        PrintCommandExample printCommand = spy(new PrintCommandExample());
        RootCommandExample rootCommand = spy(new RootCommandExample(printCommand));

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.register(rootCommand);

        commandHandler.invoke("root print set hello");

        verify(printCommand, times(1)).setDefaultPrintMessage(eq("hello"));
        verifyNoMoreInteractions(printCommand);
        verifyNoMoreInteractions(rootCommand);
    }
}
