package com.magareto.yorick.models.anime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimeResponse {
    private List<Anime> data;
    private Meta meta;


    public List<Anime> getData() {
        return data;
    }

    public void setData(List<Anime> data) {
        this.data = data;
    }
}
