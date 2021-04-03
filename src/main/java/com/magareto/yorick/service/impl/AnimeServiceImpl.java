package com.magareto.yorick.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.models.anime.Anime;
import com.magareto.yorick.models.anime.AnimeResponse;
import com.magareto.yorick.service.AnimeService;
import javassist.NotFoundException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AnimeServiceImpl implements AnimeService {
    private Logger logger = Logger.getLogger(AnimeServiceImpl.class);

    private final ObjectMapper mapper = new ObjectMapper();

    private final String KITSU_BASE_URL = "https://kitsu.io/api/edge";
    private final String ANIME_URL = "/anime";


    @Override
    public Anime getRecommendationForSeason(String season) {

        return new Anime();
    }

    @Override
    public Anime getRandomRecommendationForGenres(List<String> genres) throws IOException, YorickException {

        Map<String, List<String>> filters = new HashMap<>();
        filters.put("categories", genres);

        AnimeResponse animeResponse = findAll(filters);

        List<Anime> animeList = animeResponse.getData();

        if (animeList.isEmpty()) {
            throw new YorickException("The genre/genres could not be found, maybe you misspelled something ?");
        }

        Random random = new Random();

        int anime = random.nextInt(animeList.size());

        return animeList.get(anime);
    }

//    private Anime handleRandomization(List<Anime> animeList) {
//
//    }


    private AnimeResponse findAll(Map<String, List<String>> filters) throws IOException, YorickException {
        CloseableHttpClient client = HttpClients.createDefault();

        String preUrl = "?sort=-averageRating&page[limit]=20&page[offset]=";
        String urlEncodeFilters = urlEncodeFilters(filters);

        int count = getCountForFilters(urlEncodeFilters, client);

        if (count < 1) {
            throw new YorickException("The genre/genres could not be found, maybe you misspelled something ?");
        }

        Random random = new Random();
        int offset = random.nextInt(count);

        String url = KITSU_BASE_URL + ANIME_URL + preUrl + offset + urlEncodeFilters;

        return getAnimeResponse(url, client);

    }

    private int getCountForFilters(String filters, CloseableHttpClient client) throws IOException {
        String preUrl = "?sort=-averageRating&page[limit]=1";

        String url = KITSU_BASE_URL + ANIME_URL + preUrl + filters;

        AnimeResponse animeResponse = getAnimeResponse(url, client);

        return animeResponse.getMeta().getCount();

    }

    private AnimeResponse getAnimeResponse(String url, CloseableHttpClient httpClient) throws IOException {
        logger.info("Full URL -> " + url);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse resp = httpClient.execute(httpGet);

        return mapper.readValue(resp.getEntity().getContent(), AnimeResponse.class);

    }

    private String urlEncodeFilters(Map<String, List<String>> filters) {
        StringBuilder sb = new StringBuilder();

        int filterCount = 0;
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

            filterCount++;
        }

        return sb.toString();
    }


}
