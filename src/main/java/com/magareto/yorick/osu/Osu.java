package com.magareto.yorick.osu;

import com.magareto.yorick.osu.model.ScoreModel;

import java.io.IOException;
import java.util.List;

public interface Osu {
    List<ScoreModel> getRecentScoresForUser(String userId) throws IOException;

    String getUserIdFromLink(String link);

}
