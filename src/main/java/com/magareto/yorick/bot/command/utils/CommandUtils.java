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

        String[] splits = input.split(" ");
        return splits[0].replaceFirst("" + Constants.PREFIX, "");
    }

    public static void sendMessage(Mono<MessageChannel> channelMono, String message) {
        channelMono.flatMap(c -> c.createMessage(message)).subscribe();
    }

    public static void sendErrorMessage(Mono<MessageChannel> channel, YorickException exception) {
        StringBuilder sb = new StringBuilder();

        if(exception.getCommand() != null) {
            sb.append("Failed to execute command `").append(exception.getCommand()).append("`. ");
        }

        if(exception.getMessage() != null) {
            sb.append(exception.getMessage());
        }

        channel.flatMap(c -> c.createMessage(sb.toString())).subscribe();
    }
}
