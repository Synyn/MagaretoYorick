package com.magareto.yorick.models.anime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Relationships {
    private RelationshipEntity genres;

    public RelationshipEntity getGenres() {
        return genres;
    }

    public void setGenres(RelationshipEntity genres) {
        this.genres = genres;
    }
}
