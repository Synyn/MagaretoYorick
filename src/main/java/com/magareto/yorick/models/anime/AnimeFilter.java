package com.magareto.yorick.models.anime;

import org.codehaus.plexus.util.StringUtils;

import java.util.*;

public class AnimeFilter {
    private List<String> genres;
    private String season;

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Map<String, List<String>> toMap() {
        Map<String, List<String>> mappedFilters = new HashMap<>();

        if(genres != null && !genres.isEmpty()) {
            mappedFilters.put("genres", genres);
        }

        if(!StringUtils.isEmpty(season)) {
            mappedFilters.put("season", Collections.singletonList(season));
        }

        return mappedFilters;

    }
}
