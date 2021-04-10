package com.magareto.yorick.service;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.command.InternalCommand;
import com.magareto.yorick.bot.exception.YorickException;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import reactor.core.publisher.Mono;

import java.util.List;

public interface HelpService {
    void handleHelpCommand(CommandModel commandModel, Message message) throws YorickException;
}
