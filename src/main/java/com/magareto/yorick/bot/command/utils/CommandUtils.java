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
import reactor.core.publisher.Mono;

import java.util.*;

public class CommandUtils {

    private static Logger logger = Logger.getLogger(CommandUtils.class);

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

    public static CommandModel createCommandModelFromInput(String formattedCommand, String input) throws YorickException {
        String[] initialSplit = input.split(" ");
        String args = null;

        if (initialSplit.length >= 2) {
            args = initialSplit[1];
        }

        logger.info("args -> " + args);
        return handleCommandModelCreation(formattedCommand, args);

    }

    private static CommandModel handleCommandModelCreation(String commandName, String args) throws YorickException {

        CommandModel commandModel = new CommandModel();
        commandModel.setName(commandName);
//
//        List<String> commaArgs = null;
//        String flag = null;
//        if (args != null) {
//            String[] split = args.split("=");
//            if (split.length == 1) {
//                commaArgs = Arrays.asList(split[0].split(","));
//            } else if (split.length == 2) {
//                flag = split[0];
//                commaArgs = Arrays.asList(split[1].split(","));
//            } else {
//                throw new YorickException(ErrorMessages.INVALID_COMMAND);
//            }
//        }
//
//        commandModel.setArgs(commaArgs);

        Map<String, String> formattedArgs = new HashMap<>();

        // -m asd
        if (!args.contains("-")) {
            handleRawArgs(args, formattedArgs);

        } else {

            handleNormalArgs(args, formattedArgs);
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
        String lastArg = null;

        for (int i = 0; i < args.length(); i++) {
            char charAt = args.charAt(i);

            switch (charAt) {
                case '-': {
                    lastArg = Character.toString(args.charAt(i++));
                    formattedArgs.put(lastArg, null);
                }
                case ' ': {
                    StringBuilder fullParam = new StringBuilder();

                    while (charAt != ' ') {
                        i += 1;
                        if (i == args.length()) {
                            break;
                        }

                        charAt = args.charAt(i);
                        fullParam.append(charAt);
                    }

                    formattedArgs.put(lastArg, fullParam.toString());

                }
            }
        }
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

}
