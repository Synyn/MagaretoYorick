package com.magareto.yorick.commands;

import com.magareto.yorick.bot.command.YorickCommand;
import com.magareto.yorick.bot.command.annotations.Command;
import com.magareto.yorick.bot.command.utils.CommandUtils;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.models.anime.Anime;
import com.magareto.yorick.models.anime.Attributes;
import com.magareto.yorick.service.AnimeService;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import javassist.NotFoundException;
import org.apache.log4j.Logger;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;

@Command(name = "anime")
public class AnimeCommand implements YorickCommand {
    private Logger logger = Logger.getLogger(AnimeCommand.class);

    public static final String GENRE_ARGUMENT = "genre";

    private static final String TITLE_ENG = "en";
    private static final String TITLE_EN_US = "en_us";
    private static final String TITLE_ENG_JP = "en_jp";
    private static final String TITLE_JP = "ja_jp";


    private static final String DEFAULT_FIELD_VALUE = "Unknown";

    private AnimeService animeService = Globals.injector.getInstance(AnimeService.class);

    @Override
    public void execute(Message message) throws YorickException, IOException, NotFoundException {
        String content = message.getContent();
        String[] split = content.split("=");

        Mono<MessageChannel> channel = message.getChannel();
        if (split.length < 2) {
            CommandUtils.sendMessage(channel, "You need to provide an argument");
            return;
        }

        String argument = split[0].split(" ")[1];

        logger.info("Inside Anime command content -> " + content);
        Anime anime = handleCommand(argument, split[1]);
        sendAnime(channel, anime);
    }

    private Anime handleCommand(String argument, String filter) throws YorickException, IOException, NotFoundException {
        Anime anime = null;

        logger.info("filter");

        List<String> listedParameters = Arrays.asList(filter.split(","));

        if (argument.equals(GENRE_ARGUMENT)) {
            anime = animeService.getRandomRecommendationForGenres(listedParameters);
        }else {
            throw new YorickException(String.format(ErrorMessages.INVALID_ARGUMENT, argument));
        }

        return anime;
    }

    private void sendAnime(Mono<MessageChannel> mono, Anime anime) {

        Attributes attributes = anime.getAttributes();
        String title = handleTitle(anime.getAttributes().getTitles());
        String url = anime.getLinks().getSelf();
        String description = attributes.getDescription();
        String thumbnail = attributes.getPosterImage().getLarge();

        attributes.getTitles().forEach((k, v) -> logger.info("Key -> " + k + " Value -> " + v));

        // Fields
        String averageRating = attributes.getAverageRating();
        String date = attributes.getStartDate() + " - " + attributes.getEndDate();
        String status = attributes.getStatus();
        Integer episodeCount = attributes.getEpisodeCount();

        for (int i = 0; i < 3; i++) {
            try {
                mono.subscribe(c -> c.createEmbed(e -> e.setTitle(title)
                        .setUrl(url)
                        .setDescription(description == null ? "" : description)
                        .setThumbnail(thumbnail)
                        .addField("Average Rating", averageRating == null ? DEFAULT_FIELD_VALUE : averageRating, true)
                        .addField("Date", date.contains("null") ? DEFAULT_FIELD_VALUE : date, true)
                        .addField("Status", status == null ? DEFAULT_FIELD_VALUE : status, false)
                        .addField("Episode Count", episodeCount == null ? DEFAULT_FIELD_VALUE : Integer.toString(episodeCount), true)).block());
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private String handleTitle(Map<String, String> titles) {
        String title = titles.get(TITLE_ENG);
        if (title == null) {
            title = titles.get(TITLE_ENG_JP);
        }

        if (title == null) {
            title = titles.get(TITLE_JP);
        }
        if (title == null) {
            title = titles.get(TITLE_EN_US);
        }

        return title;
    }

}
