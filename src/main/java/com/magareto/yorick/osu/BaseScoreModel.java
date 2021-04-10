package com.magareto.yorick.osu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Calendar;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseScoreModel {
    protected Calendar scoreDate;

    public Calendar getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(Calendar scoreDate) {
        this.scoreDate = scoreDate;
    }
}
