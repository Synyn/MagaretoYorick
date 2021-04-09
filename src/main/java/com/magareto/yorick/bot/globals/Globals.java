package com.magareto.yorick.bot.globals;

import com.google.inject.Injector;
import com.magareto.yorick.bot.command.InternalCommand;

import java.util.Map;

public class Globals {
    /**
     * This is a global command map.
     * This should NOT BE used anywhere, this is for yorick`s low level work
     * If you want to add a command, you need to annotate the class with @Command
     */
    public static Map<String, InternalCommand> commands;

    /**
     * This is used for the DI managing
     */
    public static Injector injector;
}
