package com.magareto.yorick.service;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.exception.YorickException;
import discord4j.core.object.entity.Message;

public interface CommandService {
    void handleCommand(CommandModel commandModel, Message message) throws YorickException, Exception;
}
