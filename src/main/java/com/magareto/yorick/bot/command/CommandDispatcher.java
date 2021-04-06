package com.magareto.yorick.bot.command;

import com.magareto.yorick.bot.command.utils.CommandUtils;
import com.magareto.yorick.bot.constants.Constants;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.Globals;
import discord4j.core.object.entity.Message;
import org.apache.log4j.Logger;


public class CommandDispatcher {

    private static Logger logger = Logger.getLogger(CommandDispatcher.class);

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

            YorickCommand command = Globals.commands.get(formattedCommand);
            if (command == null) {
                CommandUtils.sendMessage(message.getChannel(), String.format(ErrorMessages.COMMAND_NOT_FOUND, Constants.PREFIX + formattedCommand, Constants.PREFIX + Constants.HELP_COMMAND));
                return;
            }

            CommandModel commandModel = CommandUtils.createCommandModelFromInput(formattedCommand, message.getContent());

            command.execute(message, commandModel);
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
