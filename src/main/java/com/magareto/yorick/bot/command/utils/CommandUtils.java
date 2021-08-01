package com.magareto.yorick.bot.command.utils;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.constants.Constants;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.exception.YorickException;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import org.apache.log4j.Logger;
import org.codehaus.plexus.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.*;

public class CommandUtils {

    private static Logger logger = Logger.getLogger(CommandUtils.class);

    public static String getCommandName(String input) {
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

    public static void sendErrorMessage(Mono<MessageChannel> channel, Exception e) {
        String errorMessage = e.getMessage();

        if (!(e instanceof YorickException)) {
            errorMessage = ErrorMessages.COMMAND_NOT_EXECUTABLE;
        }

        if (errorMessage == null) {
            errorMessage = ErrorMessages.INVALID_COMMAND;
        }

        String finalErrorMessage = errorMessage;

        channel.flatMap(c -> c.createMessage(finalErrorMessage)).subscribe();
    }

    public static CommandModel createCommandModelFromInput(String commandName, String args) throws YorickException {

        CommandModel commandModel = new CommandModel();
        commandModel.setName(commandName);
        Map<String, String> formattedArgs = new HashMap<>();

        // -m asd
        if (!StringUtils.isEmpty(args)) {
            if (!args.contains("-")) {
                handleRawArgs(args, formattedArgs);
            } else {
                logger.info("Inside handle normal args");
                handleNormalArgs(args, formattedArgs);
            }
        }
        for (String str : formattedArgs.keySet()) {
            logger.info("arg name -> " + str);
            logger.info("arg -> " + formattedArgs.get(str));
        }

        commandModel.setArgs(formattedArgs);

        return commandModel;
    }

    private static void handleRawArgs(String args, Map<String, String> formattedArgs) {
        String[] rawArgs = args.split(" ");

        for (String arg : rawArgs) {
            formattedArgs.put(arg, null);
        }
    }

    private static void handleNormalArgs(String args, Map<String, String> formattedArgs) {
        // -arg asd -arg asd

        String lastArg = null;
        logger.info(args.length());
        int i = 0;
        while (i < args.length()) {
            char charAt = args.charAt(i);
            if (charAt == '-') {
                i += 1;
                int index = CommandUtils.getNextWhiteSpaceIndex(i, args);
                lastArg = args.substring(i, index);

                formattedArgs.put(lastArg, null);

                i = index + 1;
            } else {
                int index = CommandUtils.getNextWhiteSpaceIndex(i, args);
                if (index + 1 == args.length()) {
                    index = args.length();
                } else {
                    while (index > args.length() || args.charAt(index) != '-') {
                        index += 1;
                    }
                }

                if (index + 1 == args.length()) {
                    index = args.length();
                }

                String param = args.substring(i, index);

                formattedArgs.put(lastArg, param);
                i = index + 1;
            }

        }

    }

    private static int getNextWhiteSpaceIndex(int i, String args) {
        int whitespaceIdx = args.length();
        for (int j = i; j < args.length(); j++) {
            if (Character.isWhitespace(args.charAt(j))) {
                whitespaceIdx = j;
                break;
            }
        }

        return whitespaceIdx;
    }

    public static boolean isAdmin(Member member) {

        logger.info("Here");

        PermissionSet permissions = member.getBasePermissions().block();
        String currentMemeber = member.getId().asString();
        logger.info("Current member id -> " + currentMemeber);

        if (currentMemeber.equals(Constants.DEV_ID)) {
            logger.info("DEV ID");
            return true;
        }

        boolean isAdmin = false;

        for (Permission permission : permissions.asEnumSet()) {
            if (permission == Permission.ADMINISTRATOR) {
                isAdmin = true;
                break;
            }
        }

        return isAdmin;

    }

    public static String excludeCommandName(String content, String commandName) {
        StringBuilder sb = new StringBuilder();

        String[] splits = content.split(" ");
        if (splits.length >= 2) {
            sb.append(content.replaceFirst(Constants.PREFIX + commandName + " ", ""));
        }

        return sb.toString();

    }
}
