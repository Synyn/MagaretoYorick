package com.magareto.yorick.bot.command;

import com.magareto.yorick.bot.exception.YorickException;
import discord4j.core.object.entity.Message;

import java.util.List;
import java.util.Map;


public abstract class YorickCommand {
    protected String commandName;
    protected String commandDescription;

    abstract public void execute(Message message, CommandModel commandModel) throws YorickException, Exception;
    abstract public List<String> getArguments();

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
