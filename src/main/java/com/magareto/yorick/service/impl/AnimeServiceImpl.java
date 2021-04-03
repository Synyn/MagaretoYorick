package com.magareto.yorick.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.models.anime.Anime;
import com.magareto.yorick.models.anime.AnimeResponse;
import com.magareto.yorick.service.AnimeService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        AnimeResponse anime = getAnimeResponse(filters);


        return new Anime();
    }

    private AnimeResponse getAnimeResponse(Map<String, List<String>> filters) throws IOException, YorickException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String filterStr = urlEncodeFilters(filters);

        String url = KITSU_BASE_URL + ANIME_URL + filterStr;
        logger.info("Full url");
        HttpGet req = new HttpGet(url);

        try {
            CloseableHttpResponse resp = httpClient.execute(req);

            return mapper.readValue(resp.getEntity().getContent(), AnimeResponse.class);
        } catch (Exception exception) {

            throw new YorickException("Error connecting to the KITS api.", exception);
        }

    }

    private String urlEncodeFilters(Map<String, List<String>> filters) {
        StringBuilder sb = new StringBuilder();

        int filterCount = 0;
        for (String key : filters.keySet()) {
            if (filterCount == 0) {
                sb.append("?filter");
            } else {
                sb.append("&filter");
            }

            sb.append("[");
            sb.append(key);
            sb.append("]=");

            filters.get(key).forEach(s -> sb.append(s.replace(" ", "%20")));

            filterCount++;
        }

        return sb.toString();
    }


}
