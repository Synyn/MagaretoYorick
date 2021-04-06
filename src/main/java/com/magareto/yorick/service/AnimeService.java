package com.magareto.yorick.service;

import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.models.anime.Anime;
import javassist.NotFoundException;

import java.io.IOException;
import java.util.List;

public interface AnimeService {
    Anime getRecommendationForSeason(String season);
    public Anime getRandomRecommendationForGenres(List<String> genres) throws YorickException;
}
