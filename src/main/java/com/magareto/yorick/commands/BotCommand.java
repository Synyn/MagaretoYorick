package com.magareto.yorick.commands;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.command.YorickCommand;
import com.magareto.yorick.bot.command.annotations.Command;
import com.magareto.yorick.bot.command.utils.CommandUtils;
import com.magareto.yorick.bot.constants.Constants;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

@Command(name = "ping", description = "Writes 'pong !' in chat.")
public class BotCommand extends YorickCommand {
    private static final Logger logger = Logger.getLogger(BotCommand.class);

    @Override
    public void execute(Message message, CommandModel commandModel) {

        User user = message.getAuthor().get();

        logger.info("Mention -> " + user.getMention());
        CommandUtils.sendMessage(message.getChannel(), user.getMention() + " pong !");
    }

    @Override
    public List<String> getArguments() {
        return null;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
