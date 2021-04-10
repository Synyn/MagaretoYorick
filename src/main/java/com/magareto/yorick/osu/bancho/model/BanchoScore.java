package com.magareto.yorick.osu.bancho.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.magareto.yorick.osu.BaseScoreModel;

import java.util.Calendar;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BanchoScore extends BaseScoreModel {
    private Integer id;
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
    private BanchoUser user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

        super.scoreDate = createdAt;
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
}
