package com.magareto.yorick.service.impl;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.command.InternalCommand;
import com.magareto.yorick.bot.constants.Constants;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.constants.Messages;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.service.HelpService;
import com.magareto.yorick.utils.BaseUtils;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelpServiceImpl implements HelpService {

    Logger logger = Logger.getLogger(HelpServiceImpl.class);

    private void handleHelpForCommands(List<InternalCommand> helpForCommands, Mono<MessageChannel> channel) {
        StringBuilder sb = new StringBuilder();

        String title = "MagaretoYorick help";
        String template = "`%s` - %s \n";

        for (InternalCommand helpForCommand : helpForCommands) {
            if (!StringUtils.isEmpty(helpForCommand.getDescription())) {
                sb.append(String.format(template, helpForCommand.getNameWithPrefix(), helpForCommand.getDescription()));
            }
        }

        channel.subscribe(c -> c.createEmbed(e -> e.setTitle(title).setDescription(sb.toString())).subscribe());

    }

    private void handleHelpForCommand(InternalCommand internalCommand, Mono<MessageChannel> channel) throws YorickException {
        if (StringUtils.isEmpty(internalCommand.getDescription())) {
            throw new YorickException(ErrorMessages.COMMAND_NOT_YET_IMPLEMENTED);
        }

        StringBuilder description = new StringBuilder();

        description.append(String.format(Messages.HELP_DESCRIPTION, internalCommand.getDescription()));


        List<String> arguments = internalCommand.getCommand().getArguments();
        // TODO Implement flags

        List<String> examples = new ArrayList<>();
        examples.add(internalCommand.getNameWithPrefix());

        if (arguments != null && !arguments.isEmpty()) {

            description.append("\n\n**Argument options** : ").append(BaseUtils.generateCommaSeparated(arguments, true));

            Random random = new Random();
            int rand = random.nextInt(arguments.size());
            String argument = arguments.get(rand);

            examples.add(internalCommand.getNameWithPrefix() + " " + argument);
        } else {
            description.append("\n");
        }


        if (examples.size() > 1) {
            description.append("\n**Usages**: ");
        } else {
            description.append("**Usage**: ");
        }

        for (int i = 0; i < examples.size(); i++) {

            description.append("`").append(examples.get(i)).append("`");

            if (i != examples.size() - 1) {
                description.append(", ");
            }

        }


        String finalDescription = description.toString();
        channel.subscribe(c -> c.createEmbed(e -> {
            e.setTitle(String.format(Messages.HELP_HEADING, internalCommand.getNameWithPrefix()))
                    .setDescription(finalDescription);
        }).subscribe());

    }

    @Override
    public void handleHelpCommand(CommandModel commandModel, Message message) throws YorickException {
        List<InternalCommand> helpForCommands = new ArrayList<>();

        logger.info("Inside help command");

        if (commandModel.getArgs() == null || commandModel.getArgs().isEmpty()) {
            Globals.commands.forEach((name, command) -> helpForCommands.add(command));
        } else if (commandModel.getArgs().size() == 1) {
            InternalCommand internalCommand = Globals.commands.get(commandModel.getArgs().get(0));

            if (internalCommand == null) {
                throw new YorickException(String.format(ErrorMessages.COMMAND_NOT_FOUND, "`" + Constants.PREFIX + commandModel.getArgs().get(0) + "`", "`" + Constants.PREFIX + Constants.HELP_COMMAND + "`"));
            }

            helpForCommands.add(internalCommand);
        } else {
            throw new YorickException(ErrorMessages.TOO_MANY_ARGUMENTS);
        }

        logger.info("Help for commands -> " + helpForCommands.size());

        if (helpForCommands.size() == 1) {
            handleHelpForCommand(helpForCommands.get(0), message.getChannel());
        } else {
            handleHelpForCommands(helpForCommands, message.getChannel());
        }

    }
}
