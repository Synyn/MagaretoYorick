package com.magareto.yorick.db.redis;

import com.magareto.yorick.bot.constants.Constants;
import com.magareto.yorick.db.redis.message.subscriber.OsuSubscriber;
import com.magareto.yorick.db.redis.model.Channel;
import discord4j.core.GatewayDiscordClient;
import redis.clients.jedis.Jedis;

public class RedisInitalizer {
    public static Jedis createConnection(String hostname) {
        return new Jedis(hostname);
    }

    public static void registerSubscribers(GatewayDiscordClient client) {
        Jedis connection = createConnection(Constants.REDIS_HOSTNAME);


        connection.subscribe(new OsuSubscriber(client), Channel.OSU.name());
    }
}
