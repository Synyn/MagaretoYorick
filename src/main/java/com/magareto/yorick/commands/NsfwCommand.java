package com.magareto.yorick.commands;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.command.YorickCommand;
import com.magareto.yorick.bot.command.annotations.Command;
import com.magareto.yorick.bot.command.utils.CommandUtils;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.service.WaifuService;
import com.magareto.yorick.service.impl.WaifuServiceImpl;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Command(name = "nsfw")
public class NsfwCommand implements YorickCommand {

    private WaifuService waifuService = Globals.injector.getInstance(WaifuService.class);

    @Override
    public void execute(Message message, CommandModel commandModel) throws YorickException, Exception {
        MessageChannel channel = message.getChannel().block();
        TextChannel tc = (TextChannel) channel;

        if (!tc.isNsfw()) {
            throw new YorickException(ErrorMessages.NOT_NSFW_CHANNEL);
        }

        String tag = null;

        if (commandModel.getArgs() != null && !commandModel.getArgs().isEmpty()) {
            tag = commandModel.getArgs().get(0);
        }

        String finalTag = tag;

        CompletableFuture.runAsync(() -> {
            try {
                String nsfw = waifuService.getContent(finalTag, true);

                message.getChannel().subscribe(c -> c.createEmbed(e -> e.setImage(nsfw)).subscribe());



            } catch (Exception e) {
                CommandUtils.sendErrorMessage(message.getChannel(), e);
            }
        });

    }
}