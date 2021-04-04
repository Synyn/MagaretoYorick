package com.magareto.yorick.bot.constants;

public class ErrorMessages {

    public static final String INVALID_ARGUMENT = "The argument `%s` was invalid.";
    public static final String INVALID_GENRE = "The genre/genres could not be found, maybe you misspelled something ?";
    public static final String INVALID_COMMAND = "Invalid command.";

    public static String COMMAND_CLASS_NOT_VALID = "There seems to be an annotated command, which does not implement the \" +\n" +
            "                        \"YorickCommand class.";
}
