package com.magareto.yorick.bot.command;

import com.magareto.yorick.bot.command.utils.CommandUtils;
import com.magareto.yorick.bot.constants.Constants;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.Globals;
import discord4j.core.object.entity.Message;
import org.apache.log4j.Logger;


public class CommandDispatcher {

    private static Logger logger = Logger.getLogger(CommandDispatcher.class);

    public static void dispatch(Message message) {
        String formattedCommand = CommandUtils.formatCommand(message.getContent());
        /**
         * This means that this was not actually a command, but just a message
         */
        logger.info("Formatted command -> " + formattedCommand);

        if (formattedCommand == null) {
            return;

        }

        YorickCommand command = Globals.commands.get(formattedCommand);

        if (command == null) {
            CommandUtils.sendMessage(message.getChannel(), "The command `" + message.getContent() + "` does not exist.");
            return;
        }

        try {
            command.execute(message);
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
