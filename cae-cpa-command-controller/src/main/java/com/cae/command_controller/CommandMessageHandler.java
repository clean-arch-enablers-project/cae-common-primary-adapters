package com.cae.command_controller;

import com.cae.command_controller.client_settings.ClientSettingsProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandMessageHandler {

    private static final Map<String, Ansi.Color> COLOR_BY_TYPE = Map.of(
            "ERROR", Ansi.Color.RED,
            "INFO", Ansi.Color.BLUE,
            "WARNING", Ansi.Color.YELLOW,
            "DEBUG", Ansi.Color.CYAN
    );

    public static void provideErrorMessage(String info){
        CommandMessageHandler.print("ERROR", info);
    }

    public static void provideInfoMessage(String info){
        CommandMessageHandler.print("INFO", info);
    }

    public static void provideWarningMessage(String info){
        CommandMessageHandler.print("WARNING", info);
    }

    public static void provideDebuggingMessage(String info){
        CommandMessageHandler.print("DEBUG", info);
    }

    private static void print(String type, String info){
        AnsiConsole.systemInstall();
        var color = CommandMessageHandler.getColorByType(type);
        CommandMessageHandler.printTypePrefix();
        System.out.print(Ansi.ansi().fg(color).a(type).reset());
        CommandMessageHandler.printTypeSuffix();
        System.out.print(info);
        System.out.println();
        AnsiConsole.systemUninstall();
    }

    private static Ansi.Color getColorByType(String type){
        return Optional.ofNullable(COLOR_BY_TYPE.get(type)).orElse(Ansi.Color.DEFAULT);
    }

    private static void printTypePrefix(){
        System.out.print(Ansi.ansi().fg(Ansi.Color.MAGENTA).a(ClientSettingsProvider.SINGLETON.getClientName()).reset());
        System.out.print("@");
        System.out.print(Ansi.ansi().fg(Ansi.Color.CYAN).a(ClientSettingsProvider.SINGLETON.getVersion()).reset());
        System.out.print(" >> [");
    }

    private static void printTypeSuffix(){
        System.out.print("] ");
    }

}
