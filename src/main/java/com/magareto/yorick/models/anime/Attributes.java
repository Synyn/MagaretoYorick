package com.magareto.yorick.models.anime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attributes {
    private String createdAt;
    private String updatedAt;
    private String synopsis;
    private String description;
    private Integer coverImageTopOffset;
    private Map<String, String> titles;
    private String canonicalTitle;
    private List<String> abbreviationTitles;
    private String averageRating;
    private Map<String, String> ratingFrequencies;
    private Integer userCount;
    private Integer favoritesCount;
    private String startDate;
    private String endDate;
    private String nextReleaseDate;
    private Integer popularityRank;
    private Integer rankRating;
    private String ageRatingGuide;
    private String subType;
    private String tba;
    private Image posterImage;
    private Image coverImage;
    private Integer episodeCount;
    private Integer episodeLength;
    private String youtubeVideoId;
    private String showType;
    private Boolean nsfw;
    private String status;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCoverImageTopOffset() {
        return coverImageTopOffset;
    }

    public void setCoverImageTopOffset(Integer coverImageTopOffset) {
        this.coverImageTopOffset = coverImageTopOffset;
    }

    public Map<String, String> getTitles() {
        return titles;
    }

    public void setTitles(Map<String, String> titles) {
        this.titles = titles;
    }

    public String getCanonicalTitle() {
        return canonicalTitle;
    }

    public void setCanonicalTitle(String canonicalTitle) {
        this.canonicalTitle = canonicalTitle;
    }

    public List<String> getAbbreviationTitles() {
        return abbreviationTitles;
    }

    public void setAbbreviationTitles(List<String> abbreviationTitles) {
        this.abbreviationTitles = abbreviationTitles;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public Map<String, String> getRatingFrequencies() {
        return ratingFrequencies;
    }

    public void setRatingFrequencies(Map<String, String> ratingFrequencies) {
        this.ratingFrequencies = ratingFrequencies;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(Integer favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getNextReleaseDate() {
        return nextReleaseDate;
    }

    public void setNextReleaseDate(String nextReleaseDate) {
        this.nextReleaseDate = nextReleaseDate;
    }

    public Integer getPopularityRank() {
        return popularityRank;
    }

    public void setPopularityRank(Integer popularityRank) {
        this.popularityRank = popularityRank;
    }

    public Integer getRankRating() {
        return rankRating;
    }

    public void setRankRating(Integer rankRating) {
        this.rankRating = rankRating;
    }

    public String getAgeRatingGuide() {
        return ageRatingGuide;
    }

    public void setAgeRatingGuide(String ageRatingGuide) {
        this.ageRatingGuide = ageRatingGuide;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getTba() {
        return tba;
    }

    public void setTba(String tba) {
        this.tba = tba;
    }

    public Image getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(Image posterImage) {
        this.posterImage = posterImage;
    }

    public Image getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public Integer getEpisodeLength() {
        return episodeLength;
    }

    public void setEpisodeLength(Integer episodeLength) {
        this.episodeLength = episodeLength;
    }

    public String getYoutubeVideoId() {
        return youtubeVideoId;
    }

    public void setYoutubeVideoId(String youtubeVideoId) {
        this.youtubeVideoId = youtubeVideoId;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public Boolean getNsfw() {
        return nsfw;
    }

    public void setNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
