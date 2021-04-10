package com.magareto.yorick.cron.osu;

import discord4j.core.GatewayDiscordClient;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.TimerTask;

public class ScoreCronJob extends TimerTask {

    private Jedis connection;

    public ScoreCronJob(Jedis connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        List<String> trackedOsuUsers = connection.lrange("trackedOsuUsers", 0, -1);

        if(trackedOsuUsers == null || trackedOsuUsers.isEmpty()) {
            return;
        }

        for(String userData: trackedOsuUsers) {

        }

    }
}
