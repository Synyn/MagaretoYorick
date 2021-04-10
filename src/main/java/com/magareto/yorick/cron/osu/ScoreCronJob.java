package com.magareto.yorick.cron.osu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.db.redis.model.Channel;
import com.magareto.yorick.db.redis.model.osu.TrackedUser;
import com.magareto.yorick.osu.BaseScoreModel;
import com.magareto.yorick.osu.Osu;
import com.magareto.yorick.osu.OsuFactory;
import com.magareto.yorick.osu.OsuServer;
import com.magareto.yorick.osu.bancho.model.OsuScore;
import redis.clients.jedis.Jedis;

import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

public class ScoreCronJob extends TimerTask {

    private Jedis connection;

    private ObjectMapper mapper = new ObjectMapper();

    public ScoreCronJob(Jedis connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        List<String> trackedOsuUsers = connection.lrange("trackedOsuUsers", 0, -1);

        if (trackedOsuUsers == null || trackedOsuUsers.isEmpty()) {
            return;
        }

        for (String trackedUser : trackedOsuUsers) {
            try {
                TrackedUser user = mapper.readValue(trackedUser, TrackedUser.class);
                Osu osu = OsuFactory.createOsu(user.getServer());
                List<BaseScoreModel> recentScoresForUser = osu.getRecentScoresForUser(user.getUserId());

                handleScorePublish(user, recentScoresForUser);
            } catch (JsonProcessingException | YorickException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleScorePublish(TrackedUser user, List<BaseScoreModel> recentScoresForUser) throws JsonProcessingException {
        Calendar userLastScoreDate = user.getLastScoreDate();

        for (BaseScoreModel recentScore : recentScoresForUser) {
            if (recentScore.getScoreDate().after(userLastScoreDate)) {
                userLastScoreDate = recentScore.getScoreDate();
                connection.publish(Channel.OSU.name(), mapper.writeValueAsString(recentScore));
            }
        }
    }
}
