package com.magareto.yorick.service;

import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.models.anime.Anime;

import java.util.List;
import java.util.Map;

public interface AnimeService {
    Anime getRecommendationForSeason(String season);
    public Anime getRandomAnimeForFilters(Map<String, List<String>> genres) throws YorickException;
}
