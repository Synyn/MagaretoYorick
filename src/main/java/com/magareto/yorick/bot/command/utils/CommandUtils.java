package com.magareto.yorick.bot.command.utils;

import com.magareto.yorick.bot.constants.Constants;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.utils.BaseUtils;
import discord4j.core.object.entity.channel.MessageChannel;
import reactor.core.publisher.Mono;

public class CommandUtils {

    public static String formatCommand(String input) {
        if (!input.startsWith(Constants.PREFIX)) {
            return null;
        }

        return input.replaceFirst("\\" + Constants.PREFIX, "");
    }

    public static void sendMessage(Mono<MessageChannel> channelMono, String message) {
        channelMono.flatMap(c -> c.createMessage(message)).subscribe();
    }

    public static void sendErrorMessage(Mono<MessageChannel> channel, YorickException exception) {
        String message = "Failed to execute command -> " + exception.getCommand() + ". " + exception.getMessage();
        channel.flatMap(c -> c.createMessage(message)).subscribe();
    }
}
