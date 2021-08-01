package com.magareto.yorick.commands;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.command.YorickCommand;
import com.magareto.yorick.bot.command.annotations.Command;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.service.MemeService;
import discord4j.core.object.entity.Message;

import java.util.List;

@Command(name = "meme", description = "Sends a meme in the chat.")
public class MemeCommand extends YorickCommand {

    public static MemeService memeService = Globals.injector.getInstance(MemeService.class);

    @Override
    public void execute(Message message, CommandModel commandModel) throws YorickException, Exception {
        String link = null;

        if (commandModel.getArgs() == null || commandModel.getArgs().isEmpty()) {
            link = memeService.getMeme();
        } else {
            link = memeService.getMeme(commandModel.getArgs().get(0));
        }

        String finalLink = link;
        message.getChannel().subscribe(c -> c.createEmbed(e -> e.setImage(finalLink)).subscribe());

    }

    @Override
    public List<String> getArguments() {
        return null;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
