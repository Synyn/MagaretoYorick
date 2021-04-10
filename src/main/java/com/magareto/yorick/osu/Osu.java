package com.magareto.yorick.osu;

import com.magareto.yorick.osu.model.ScoreModel;

import java.util.List;

public interface Osu {
    List<ScoreModel> getRecentScoresForUser(String userId);

    String getUserIdFromLink(String link);

}
