package com.magareto.yorick.commands;

import com.magareto.yorick.bot.command.CommandModel;
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
import java.util.concurrent.CompletableFuture;

@Command(name = "anime")
public class AnimeCommand implements YorickCommand {
    private Logger logger = Logger.getLogger(AnimeCommand.class);

    public static final Set<String> GENRE_ARGUMENT_LIST = new HashSet<>(Arrays.asList("genre", "genres"));

    private static final String TITLE_ENG = "en";
    private static final String TITLE_EN_US = "en_us";
    private static final String TITLE_ENG_JP = "en_jp";
    private static final String TITLE_JP = "ja_jp";


    private static final String DEFAULT_FIELD_VALUE = "Unknown";

    private AnimeService animeService = Globals.injector.getInstance(AnimeService.class);

    @Override
    public void execute(Message message, CommandModel commandModel) {

        logger.info("Inside Anime command content -> " + message.getContent());

        CompletableFuture.runAsync(() -> {
            try {
                Anime anime = handleCommand(commandModel.getFlag(), commandModel.getArgs());
                sendAnime(message.getChannel(), anime);
            } catch (Exception e) {
                e.printStackTrace();
                CommandUtils.sendErrorMessage(message.getChannel(), e);
            }
        });
    }

    private Anime handleCommand(String flag, List<String> arguments) throws YorickException {
        Anime anime = null;

        if (flag == null || GENRE_ARGUMENT_LIST.contains(flag)) {
            anime = animeService.getRandomRecommendationForGenres(arguments);
        } else {
            throw new YorickException(ErrorMessages.INVALID_ARGUMENT);
        }

        return anime;
    }

    private void sendAnime(Mono<MessageChannel> mono, Anime anime) {

        Attributes attributes = anime.getAttributes();
        String title = handleTitle(anime.getAttributes().getTitles());
        String url = anime.getLinks().getSelf();
        String description = attributes.getDescription();
        String thumbnail = attributes.getPosterImage().getLarge();
        String genres = handleListings(anime.getGenres());

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
                        .addField("Genres", genres, true)
                        .addField("Average Rating", averageRating == null ? DEFAULT_FIELD_VALUE : averageRating, true)
                        .addField("Date", date.contains("null") ? DEFAULT_FIELD_VALUE : date, false)
                        .addField("Status", status == null ? DEFAULT_FIELD_VALUE : status, true)
                        .addField("Episode Count", episodeCount == null ? DEFAULT_FIELD_VALUE : Integer.toString(episodeCount), true)).block());
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String handleListings(List<String> listings) {
        StringBuilder sb = new StringBuilder();
        if (listings != null) {
            for (int i = 0; i < listings.size(); i++) {

                sb.append(listings.get(i));

                if (i != listings.size() - 1) {
                    sb.append(", ");
                }
            }
        }

        return sb.toString();
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
