package com.magareto.yorick.bot.command;

import com.magareto.yorick.bot.constants.Constants;

public class InternalCommand {
    private String name;
    private String description;
    private YorickCommand command;

    public InternalCommand(String name, String description, YorickCommand command) {
        this.name = name;
        this.description = description;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YorickCommand getCommand() {
        return command;
    }

    public void setCommand(YorickCommand command) {
        this.command = command;
    }

    public String getNameWithPrefix() {
        return Constants.PREFIX + this.name;
    }
}
