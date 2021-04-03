package com.magareto.yorick.bot.command;

import com.magareto.yorick.bot.exception.YorickException;
import discord4j.core.object.entity.Message;

public interface YorickCommand {
    void execute(Message message) throws YorickException, Exception;
}
