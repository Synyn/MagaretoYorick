package com.magareto.yorick.osu;

import java.util.List;

public interface Osu {
    List<BaseScoreModel> getRecentScoresForUser(String userId);

    String getUserIdFromLink(String link);

}
