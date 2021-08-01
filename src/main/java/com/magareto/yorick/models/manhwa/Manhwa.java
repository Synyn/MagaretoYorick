package com.magareto.yorick.models.manhwa;

import java.util.Objects;

public class Manhwa {
    private String id;
    private String title;
    private String alias;
    private String summary;
    private String genres;
    private String authors;
    private String seriesLink;
    private String imageLink;
    private String rank;
    private String totalViews;
    private String monthlyViews;
    private String releaseStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Objects.requireNonNullElse(title, "");
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = Objects.requireNonNullElse(alias, "");
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = Objects.requireNonNullElse(summary, "");
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = Objects.requireNonNullElse(genres, "");
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = Objects.requireNonNullElse(authors, "");
    }

    public String getSeriesLink() {
        return seriesLink;
    }

    public void setSeriesLink(String seriesLink) {
        this.seriesLink = Objects.requireNonNullElse(seriesLink, " ");
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = Objects.requireNonNullElse(imageLink, "");
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = Objects.requireNonNullElse(rank, " ");
    }

    public String getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(String totalViews) {
        this.totalViews = Objects.requireNonNullElse(totalViews, " ");
    }

    public String getMonthlyViews() {
        return monthlyViews;
    }

    public void setMonthlyViews(String monthlyViews) {
        this.monthlyViews = Objects.requireNonNullElse(monthlyViews, " ");
    }

    public String getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(String releaseStatus) {
        this.releaseStatus = Objects.requireNonNullElse(releaseStatus, " ");
    }
}
