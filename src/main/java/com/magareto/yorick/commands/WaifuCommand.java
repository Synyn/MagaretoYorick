package com.magareto.yorick.commands;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.command.YorickCommand;
import com.magareto.yorick.bot.command.annotations.Command;
import com.magareto.yorick.bot.command.utils.CommandUtils;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.service.WaifuService;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import org.apache.log4j.Logger;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Command(name = "waifu")
public class WaifuCommand implements YorickCommand {

    private WaifuService waifuService = Globals.injector.getInstance(WaifuService.class);

    private Logger logger = Logger.getLogger(WaifuCommand.class);

    private Runnable executeTask(Mono<MessageChannel> channel, CommandModel commandModel) throws IOException {


        return null;
    }

    @Override
    public void execute(Message message, CommandModel commandModel) {
        String tag = null;

        if (commandModel.getArgs() != null && !commandModel.getArgs().isEmpty()) {
            tag = commandModel.getArgs().get(0);
        }

        String finalTag = tag;
        CompletableFuture.runAsync(() -> {
            try {
                String sfw = waifuService.getContent(finalTag, false);

                message.getChannel().subscribe(c -> c.createEmbed(e -> e.setImage(sfw)).subscribe());

            } catch (Exception e) {
                e.printStackTrace();
                CommandUtils.sendErrorMessage(message.getChannel(), e);
            }
        });
    }
}
