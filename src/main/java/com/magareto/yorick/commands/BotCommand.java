package com.magareto.yorick.commands;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.command.YorickCommand;
import com.magareto.yorick.bot.command.annotations.Command;
import com.magareto.yorick.bot.command.utils.CommandUtils;
import discord4j.core.object.entity.Message;
import org.apache.log4j.Logger;

@Command(name = "ping")
public class BotCommand implements YorickCommand {
    private static final Logger logger = Logger.getLogger(BotCommand.class);
    @Override
    public void execute(Message message, CommandModel commandModel) {
        CommandUtils.sendMessage(message.getChannel(), "PONG!");
    }
}
