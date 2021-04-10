package com.magareto.yorick.cron;

import com.magareto.yorick.cron.osu.ScoreCronJob;
import discord4j.core.GatewayDiscordClient;
import redis.clients.jedis.Jedis;

import java.util.Timer;

public class CronInitializer {

    public static void registerCronJobs(GatewayDiscordClient client, Jedis connection) {
        Timer timer = new Timer();

        // Register the osu score polling cron job
        timer.scheduleAtFixedRate(new ScoreCronJob(connection), 0, 10000);
    }

}
