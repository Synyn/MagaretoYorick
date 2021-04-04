package com.magareto.yorick.models.anime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimeResponse {
    private List<Anime> data;
    private Meta meta;
    private List<Inclusion> included;


    public List<Anime> getData() {
        return data;
    }

    public void setData(List<Anime> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Inclusion> getIncluded() {
        return included;
    }

    public void setIncluded(List<Inclusion> included) {
        this.included = included;
    }
}
