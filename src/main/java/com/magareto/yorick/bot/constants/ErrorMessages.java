package com.magareto.yorick.bot.constants;

public class ErrorMessages {

    public static final String INVALID_ARGUMENT = "The argument `%s` was invalid.";
    public static final String INVALID_GENRE = "The genre/genres could not be found, maybe you misspelled something ?";
    public static final String INVALID_COMMAND = "Invalid command.";
    public static final String INVALID_BET = "Invalid bet. The bets cane be `heads`, `tails`, `even`.";
    public static final String INVALID_FLAG = "You did not provide a flag, or the command does not have a default one.";
    public static final String COMMAND_NOT_FOUND = "The command %s was not found. Type %s for more information.";
    public static final String TOO_MANY_ARGUMENTS = "You provided too many arguments.";
    public static final String NOT_ENOUGH_ARGUMENTS = "You did not provide the needed number of arguments.";
    public static final String  CONNECTION_ERROR = "There seems to be an error with the connection.";
    public static final String WAIFU_NOT_FOUND = "The waifu was not found...";
    public static final String COMMAND_NOT_EXECUTABLE = "Command is not executable.";
    public static final String NOT_NSFW_CHANNEL = "This channel is not an nsfw channel, I cannot send nsfw content here.";
    public static final String COMMAND_NOT_YET_IMPLEMENTED = "This command is not implemented yet.";
    public static final String NOT_FOUND = "This resource was not found.";


    public static final String INVALID_OSU_SERVER = "The server that is specified in this message seems to be invalid.";
    public static String COMMAND_CLASS_NOT_VALID = "There seems to be an annotated command, which does not implement the \" +\n" +
            "                        \"YorickCommand class.";
}
