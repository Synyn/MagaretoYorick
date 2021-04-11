package com.magareto.yorick.osu.model;

import com.magareto.yorick.osu.OsuServer;

import java.util.Calendar;
import java.util.List;

public class ScoreModel {
    private String id;
    private OsuServer server;

    private String userId;
    private List<String> mods;
    private Integer score;
    private Integer maxCombo;
    private String rank;
    private Calendar createdAt;
    private String pp;
    private String gameMode;
    private String beatMapUrl;
    private String username;
    private String bpm;
    private String starRating;
    private String difficulty;
    private String beatMapName;
    private String userAvatarUrl;
    private String missCount;
    private String accuracy;

    public String getMissCount() {
        return missCount;
    }

    public void setMissCount(String missCount) {
        this.missCount = missCount;
    }

    public String getBpm() {
        return bpm;
    }

    public void setBpm(String bpm) {
        this.bpm = bpm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getBeatMapUrl() {
        return beatMapUrl;
    }

    public void setBeatMapUrl(String beatMapUrl) {
        this.beatMapUrl = beatMapUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public OsuServer getServer() {
        return server;
    }

    public void setServer(OsuServer server) {
        this.server = server;
    }

    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getBeatMapName() {
        return beatMapName;
    }

    public void setBeatMapName(String beatMapName) {
        this.beatMapName = beatMapName;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }
}
