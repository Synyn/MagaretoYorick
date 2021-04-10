package com.magareto.yorick.osu;

import com.magareto.yorick.osu.bancho.model.OsuScore;

import java.util.List;

public interface Osu {
    List<BaseScoreModel> getRecentScoresForUser(String userId);
}
