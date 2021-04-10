package com.magareto.yorick.osu;

import java.util.Calendar;

public class BaseScoreModel {
    protected Calendar scoreDate;
    private OsuServer server;

    private String json;

    public Calendar getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(Calendar scoreDate) {
        this.scoreDate = scoreDate;
    }

    public OsuServer getServer() {
        return server;
    }

    public void setServer(OsuServer server) {
        this.server = server;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
