package com.magareto.yorick.commands;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.command.YorickCommand;
import com.magareto.yorick.bot.command.annotations.Command;
import com.magareto.yorick.bot.exception.YorickException;
import discord4j.core.object.entity.Message;

import java.util.List;

@Command(name = "meme")
public class MemeCommand implements YorickCommand {
    @Override
    public void execute(Message message, CommandModel commandModel) throws YorickException, Exception {

    }

    @Override
    public List<String> getArguments() {
        return null;
    }
}
