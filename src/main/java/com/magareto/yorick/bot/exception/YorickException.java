package com.magareto.yorick.bot.exception;

import com.magareto.yorick.utils.BaseUtils;

public class YorickException extends Exception {

    private String command;
    private String message;

    public YorickException() {}

    public YorickException(String message) {
        this.message = message;
    }

    public YorickException(String command, String message) {
        super("Failure for command " + command + ". " + message);
        this.command = command;
        this.message = message;
    }

    public YorickException(String message, Exception e) {
        super(message + " " + BaseUtils.getStackTrace(e));
        this.message = message;
        e.printStackTrace();
    }

    public YorickException(String command, String message, Exception e) {
        super("Failure for command " + command + ". " + message + " Stack Trace -> " + BaseUtils.getStackTrace(e));
        this.command = command;
        this.message = message;
    }

    public YorickException(Exception e) {
        super(e);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
