package com.magareto.yorick.service.impl;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.command.InternalCommand;
import com.magareto.yorick.bot.command.utils.CommandUtils;
import com.magareto.yorick.bot.constants.Constants;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.service.CommandService;
import discord4j.core.object.entity.Message;

public class CommandServiceImpl implements CommandService {
    @Override
    public void handleCommand(CommandModel commandModel, Message message) throws YorickException, Exception {
        InternalCommand internalCommand = Globals.commands.get(commandModel.getName());

        if (internalCommand == null) {
            CommandUtils.sendMessage(message.getChannel(), String.format(ErrorMessages.COMMAND_NOT_FOUND, "`" + Constants.PREFIX + commandModel.getName() + "`", "`" + Constants.PREFIX + Constants.HELP_COMMAND + "`"));
            return;
        }

        internalCommand.getCommand().execute(message, commandModel);
    }
}
