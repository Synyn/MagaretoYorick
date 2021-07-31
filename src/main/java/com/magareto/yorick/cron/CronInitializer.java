package com.magareto.yorick.cron;

import com.magareto.yorick.bot.constants.RedisConstants;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.cron.osu.ScoreCronJob;
import com.magareto.yorick.db.redis.RedisInitalizer;
import discord4j.core.GatewayDiscordClient;
import redis.clients.jedis.Jedis;

import java.util.Timer;

public class CronInitializer {

    public static void registerCronJobs() {

        // Register the osu score polling cron job

        Thread thread = new Thread(() -> {

            Jedis connection = RedisInitalizer.createConnection(RedisConstants.HOSTNAME);

            Timer timer = new Timer();

            if (Globals.bancho) {
                timer.scheduleAtFixedRate(new ScoreCronJob(connection), 0, 10000);
            }
        });

        thread.start();


    }

}
