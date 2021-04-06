package com.magareto.yorick.bot.command.utils;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.constants.Constants;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.exception.YorickException;
import discord4j.core.object.entity.channel.MessageChannel;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandUtils {

    public static String formatCommand(String input) {
        if (!input.startsWith(Constants.PREFIX)) {
            return null;
        }

        String[] splits = input.split(" ");
        return splits[0].replaceFirst("" + Constants.PREFIX, "");
    }

    public static void sendMessage(Mono<MessageChannel> channelMono, String message) {
        channelMono.flatMap(c -> c.createMessage(message)).subscribe();
    }

    public static void sendErrorMessage(Mono<MessageChannel> channel, YorickException exception) {
        StringBuilder sb = new StringBuilder();

        if (exception.getCommand() != null) {
            sb.append("Failed to execute command `").append(exception.getCommand()).append("`. ");
        }

        if (exception.getMessage() != null) {
            sb.append(exception.getMessage());
        }

        channel.flatMap(c -> c.createMessage(sb.toString())).subscribe();
    }

    public static CommandModel createCommandModelFromInput(String formattedCommand, String input) throws YorickException {

        String[] initialSplit = input.split(" ");

//        if (initialSplit.length < 2) {
//            throw new YorickException(ErrorMessages.INVALID_FLAG);
//        }

        return handleCommandModelCreation(formattedCommand, initialSplit[1]);

    }

    private static CommandModel handleCommandModelCreation(String commandName, String args) throws YorickException {

        CommandModel commandModel = new CommandModel();
        commandModel.setName(commandName);

        List<String> commaArgs = null;
        String flag = null;
        String[] split = args.split("=");
        if (split.length == 1) {
            commaArgs = Arrays.asList(split[0].split(","));

        } else if (split.length == 2) {
            flag = split[0];
            commaArgs = Arrays.asList(split[1].split(","));
        } else {
            throw new YorickException(ErrorMessages.INVALID_COMMAND);
        }

        commandModel.setArgs(commaArgs);
        commandModel.setFlag(flag);

        return commandModel;
    }

}
