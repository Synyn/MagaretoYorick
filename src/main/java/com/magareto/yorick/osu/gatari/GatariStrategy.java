package com.magareto.yorick.osu.gatari;

import com.magareto.yorick.osu.Osu;
import com.magareto.yorick.osu.model.ScoreModel;

import java.util.List;

public class GatariStrategy implements Osu {

    private final static String BASE_URL = "https://api.gatari.pw";
    private final static String SCORE_URL = "/user/scores/recent?id=";

    @Override
    public List<ScoreModel> getRecentScoresForUser(String userId) {
        return null;
    }

    @Override
    public String getUserIdFromLink(String link) {
        return null;
    }
}
