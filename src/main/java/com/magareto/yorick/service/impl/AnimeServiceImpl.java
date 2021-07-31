package com.magareto.yorick.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.bot.constants.Constants;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.models.anime.*;
import com.magareto.yorick.service.AnimeService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class AnimeServiceImpl implements AnimeService {
    private Logger logger = Logger.getLogger(AnimeServiceImpl.class);

    private final ObjectMapper mapper = new ObjectMapper();

    private final String KITSU_BASE_URL = "https://kitsu.io/api/edge";
    private final String ANIME_URL = "/anime";

    private static final int STATUS_SUCCESS = 200;

    @Override
    public Anime getRecommendationForSeason(String season) {

        return new Anime();
    }

    @Override
    public Anime getRandomAnimeForFilters(Map<String, List<String>> filters) throws YorickException {

        AnimeResponse animeResponse = findAll(filters);
        List<Anime> animeList = animeResponse.getData();

        if (animeList.isEmpty()) {
            throw new YorickException(ErrorMessages.INVALID_GENRE);
        }

        Random random = new Random();

        int animeIndex = random.nextInt(animeList.size());

        Anime anime = animeList.get(animeIndex);

        anime.setGenres(getGenresForAnime(animeResponse.getIncluded(), anime));

        return anime;
    }

    private List<String> getGenresForAnime(List<Inclusion> included, Anime anime) {
        List<String> genres = new ArrayList<>();

        List<RelationShipDataEntity> genreData = anime.getRelationships().getGenres().getData();
        for (RelationShipDataEntity genreRelation : genreData) {
            for (Inclusion inclusion : included) {
                if (!inclusion.getType().equals("genres")) {
                    continue;
                }

                if (!inclusion.getId().equals(genreRelation.getId())) {
                    continue;
                }

                genres.add(inclusion.getAttributes().getName());

            }
        }

        return genres;

    }


    private AnimeResponse findAll(Map<String, List<String>> filters) throws YorickException {
        CloseableHttpClient client = HttpClients.createDefault();

        String preUrl = "?sort=-averageRating&include=genres&page[limit]=20&page[offset]=";
        String urlEncodeFilters = urlEncodeFilters(filters);

        int count = getCountForFilters(urlEncodeFilters, client);

        if (count < 1) {
            throw new YorickException("The genre/genres could not be found, maybe you misspelled something ?");
        }

        Random random = new Random();
        int offset = random.nextInt(count);

        String url = KITSU_BASE_URL + ANIME_URL + preUrl + offset + urlEncodeFilters;

        try {
            return getAnimeResponse(url, client);
        } catch (IOException e) {
            e.printStackTrace();
            throw new YorickException(ErrorMessages.CONNECTION_ERROR);
        }

    }

    private int getCountForFilters(String filters, CloseableHttpClient client) throws YorickException {
        String preUrl = "?sort=-averageRating&page[limit]=1";

        String url = KITSU_BASE_URL + ANIME_URL + preUrl + filters;

        AnimeResponse animeResponse = null;
        try {
            animeResponse = getAnimeResponse(url, client);
        } catch (IOException e) {
            e.printStackTrace();
            throw new YorickException(ErrorMessages.CONNECTION_ERROR);
        }

        return animeResponse.getMeta().getCount();

    }

    private AnimeResponse getAnimeResponse(String url, CloseableHttpClient httpClient) throws IOException, YorickException {
        logger.info("Full URL -> " + url);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse resp = httpClient.execute(httpGet);

        if (resp.getStatusLine().getStatusCode() == Constants.STATUS_NOT_FOUND) {
            throw new YorickException(ErrorMessages.INVALID_GENRE);
        }

        return mapper.readValue(resp.getEntity().getContent(), AnimeResponse.class);

    }

    private String urlEncodeFilters(Map<String, List<String>> filters) {
        StringBuilder sb = new StringBuilder();

        for (String key : filters.keySet()) {
            sb.append("&filter");

            sb.append("[");
            sb.append(key);
            sb.append("]=");

            if (filters.get(key) != null) {
                int argsCounter = 0;
                for (String filter : filters.get(key)) {
                    if (argsCounter > 0) {
                        filter = "," + filter;
                    }
                    sb.append(filter.replace(" ", "%20"));
                    argsCounter++;
                }
            }
        }

        return sb.toString();
    }


}
