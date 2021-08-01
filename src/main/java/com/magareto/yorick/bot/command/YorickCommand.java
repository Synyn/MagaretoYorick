package com.magareto.yorick.bot.command;

import com.magareto.yorick.bot.exception.YorickException;
import discord4j.core.object.entity.Message;

import java.util.List;
import java.util.Map;


public abstract class YorickCommand {
    protected String commandName;
    protected String commandDescription;

    public abstract void execute(Message message, CommandModel commandModel) throws YorickException, Exception;
    public abstract List<String> getArguments();

    // There is a auto generated help command, but if you are not happy with it you can use this instead.
    public abstract String getHelp();

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    public void setCommandDescription(String commandDescription) {
        this.commandDescription = commandDescription;
    }
}
