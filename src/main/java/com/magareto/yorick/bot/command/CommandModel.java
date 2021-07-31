package com.magareto.yorick.bot.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandModel {
    private String name;
    private Map<String, String> args;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }
}
