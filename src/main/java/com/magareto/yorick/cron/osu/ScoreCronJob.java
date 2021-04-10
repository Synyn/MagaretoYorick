package com.magareto.yorick.cron.osu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.bot.constants.RedisConstants;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.db.redis.model.Channel;
import com.magareto.yorick.db.redis.model.osu.TrackedUser;
import com.magareto.yorick.osu.BaseScoreModel;
import com.magareto.yorick.osu.Osu;
import com.magareto.yorick.osu.OsuFactory;
import com.magareto.yorick.osu.model.ScoreModel;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

public class ScoreCronJob extends TimerTask {

    private Jedis connection;

    private ObjectMapper mapper = new ObjectMapper();

    Logger logger = Logger.getLogger(ScoreCronJob.class);

    public ScoreCronJob(Jedis connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        Set<String> trackedOsuUsers = connection.smembers("trackedOsuUsers");

        logger.info("Cron job found users -> " + trackedOsuUsers);
        if (trackedOsuUsers == null || trackedOsuUsers.isEmpty()) {
            return;
        }

        for (String trackedUser : trackedOsuUsers) {
            try {
                logger.info("Tracked User -> " + trackedUser);

                TrackedUser user = mapper.readValue(trackedUser, TrackedUser.class);

                logger.info("Server -> " + user.getServer());

                Osu osu = OsuFactory.createOsu(user.getServer());
                List<ScoreModel> recentScoresForUser = osu.getRecentScoresForUser(user.getUserId());

                handleScorePublish(user, recentScoresForUser);
            } catch (JsonProcessingException | YorickException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleScorePublish(TrackedUser user, List<ScoreModel> recentScoresForUser) throws JsonProcessingException {
        if (recentScoresForUser == null || recentScoresForUser.isEmpty()) {
            return;
        }

        Calendar userLastScoreDate = (Calendar) user.getLastScoreDate().clone();
        boolean update = false;

        logger.info("User current score -> " + userLastScoreDate.getTime().toString());

        for (ScoreModel recentScore : recentScoresForUser) {

            logger.info("Recent score -> " + mapper.writeValueAsString(recentScore));

            if (recentScore.getCreatedAt().after(userLastScoreDate)) {
                logger.info("New score -> " + recentScore.getCreatedAt().getTime().toString());
                logger.info("Publishing score -> " + recentScore.getCreatedAt());

                userLastScoreDate = recentScore.getCreatedAt();
                update = true;
                connection.publish(Channel.OSU.name(), mapper.writeValueAsString(recentScore));
            }
        }

        if (update) {
            connection.srem(RedisConstants.OSU_TRACK_LIST, mapper.writeValueAsString(user));
            user.setLastScoreDate(userLastScoreDate);
            connection.sadd(RedisConstants.OSU_TRACK_LIST, mapper.writeValueAsString(user));
        }
    }
}
