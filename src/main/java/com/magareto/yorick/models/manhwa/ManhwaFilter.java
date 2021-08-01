package com.magareto.yorick.models.manhwa;

import java.util.List;

public class ManhwaFilter {
    private List<String> genres;
    private String title;
    private boolean top;

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }
}
