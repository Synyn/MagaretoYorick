package com.magareto.yorick.osu.bancho.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.magareto.yorick.osu.BaseScoreModel;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BanchoScore {
    private Double accuracy;
    private Long id;
    @JsonProperty("user_id")
    private Integer userId;
    private List<String> mods;
    private Integer score;
    @JsonProperty("max_combo")
    private Integer maxCombo;
    private String rank;
    @JsonProperty("created_at")
    private Calendar createdAt;
    private Double pp;
    private String mode;
    private BanchoBeatmap beatmap;
    @JsonProperty("beatmapset")
    private BeatmapSet beatmapSet;
    private BanchoUser user;
    private Map<String, Integer> statistics;

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<String> getMods() {
        return mods;
    }

    public void setMods(List<String> mods) {
        this.mods = mods;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getMaxCombo() {
        return maxCombo;
    }

    public void setMaxCombo(Integer maxCombo) {
        this.maxCombo = maxCombo;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;

    }

    public Double getPp() {
        return pp;
    }

    public void setPp(Double pp) {
        this.pp = pp;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public BanchoBeatmap getBeatmap() {
        return beatmap;
    }

    public void setBeatmap(BanchoBeatmap banchoBeatmap) {
        this.beatmap = banchoBeatmap;
    }

    public BanchoUser getUser() {
        return user;
    }

    public void setUser(BanchoUser user) {
        this.user = user;
    }

    public BeatmapSet getBeatmapSet() {
        return beatmapSet;
    }

    public void setBeatmapSet(BeatmapSet beatmapSet) {
        this.beatmapSet = beatmapSet;
    }

    public Map<String, Integer> getStatistics() {
        return statistics;
    }

    public void setStatistics(Map<String, Integer> statistics) {
        this.statistics = statistics;
    }
}
