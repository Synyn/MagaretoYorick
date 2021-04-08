package com.magareto.yorick.bot.command;

import com.magareto.yorick.bot.exception.YorickException;
import discord4j.core.object.entity.Message;

import java.util.List;
import java.util.Map;


public interface YorickCommand {
    void execute(Message message, CommandModel commandModel) throws YorickException, Exception;

    List<String> getArguments();
}
