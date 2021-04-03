package com.magareto.yorick.commands;

import com.magareto.yorick.bot.command.YorickCommand;
import com.magareto.yorick.bot.command.annotations.Command;
import com.magareto.yorick.bot.command.utils.CommandUtils;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.models.anime.Anime;
import com.magareto.yorick.service.AnimeService;
import discord4j.core.object.entity.Message;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Command(name = "anime")
public class AnimeCommand implements YorickCommand {
    private Logger logger = Logger.getLogger(AnimeCommand.class);

    public static final String GENRE_ARGUMENT = "genre";

    private AnimeService animeService = Globals.injector.getInstance(AnimeService.class);

    @Override
    public void execute(Message message) throws YorickException, IOException {
        String content = message.getContent();

        String[] commandParts = content.split(" ");

        if (commandParts.length < 3) {
            CommandUtils.sendMessage(message.getChannel(), "You need to provide an argument");
            return;
        }

        logger.info("Inside Anime command content -> " + content);


        Anime anime = handleArguments(commandParts[0], commandParts[1]);


    }

    private Anime handleArguments(String argument, String filter) throws YorickException, IOException {
        Anime anime = null;

        List<String> listedParameters = Arrays.asList(filter.split(","));
        if (argument.equals(GENRE_ARGUMENT)) {

            anime = animeService.getRandomRecommendationForGenres(listedParameters);

        }

        return anime;
    }
}
