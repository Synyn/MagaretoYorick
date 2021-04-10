package com.magareto.yorick.bot.command;

import com.magareto.yorick.bot.command.utils.CommandUtils;
import com.magareto.yorick.bot.constants.Constants;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.constants.Messages;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.osu.OsuServer;
import com.magareto.yorick.service.CommandService;
import com.magareto.yorick.service.HelpService;
import com.magareto.yorick.service.OsuService;
import com.magareto.yorick.utils.BaseUtils;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CommandDispatcher {

    private static Logger logger = Logger.getLogger(CommandDispatcher.class);

    private static HelpService helpService = Globals.injector.getInstance(HelpService.class);
    private static CommandService commandService = Globals.injector.getInstance(CommandService.class);

    public static void dispatch(Message message) {

        String formattedCommand = CommandUtils.formatCommand(message.getContent());

        try {
            logger.info("Formatted command -> " + formattedCommand);
            /**
             * This means that this was not actually a command, but just a message
             */
            if (formattedCommand == null) {
                return;
            }

            CommandModel commandModel = CommandUtils.createCommandModelFromInput(formattedCommand, message.getContent());

            if (!formattedCommand.equals(Constants.HELP_COMMAND)) {
                commandService.handleCommand(commandModel, message);
            } else {
                helpService.handleHelpCommand(commandModel, message);
            }

        } catch (YorickException exception) {
            exception.printStackTrace();

            CommandUtils.sendErrorMessage(message.getChannel(), exception);
        } catch (Exception e) {
            e.printStackTrace();
            YorickException yorickException = new YorickException();
            yorickException.setCommand(formattedCommand);

            CommandUtils.sendErrorMessage(message.getChannel(), yorickException);
        }
    }

}
