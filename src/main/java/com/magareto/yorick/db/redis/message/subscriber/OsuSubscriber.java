package com.magareto.yorick.db.redis.message.subscriber;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.db.redis.model.Channel;
import discord4j.core.GatewayDiscordClient;
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisPubSub;

public class OsuSubscriber extends JedisPubSub {
    private final GatewayDiscordClient client;

    ObjectMapper mapper = new ObjectMapper();

    Logger logger = Logger.getLogger(OsuSubscriber.class);

    public OsuSubscriber(GatewayDiscordClient client) {
        this.client = client;
    }

    @Override
    public void onMessage(String channel, String message) {

        logger.info("Osu -> " + message);

        Channel ch = Channel.valueOf(channel);

        switch (ch) {
            case OSU -> handleOsuScore(client, message);
        }
    }

    private void handleOsuScore(GatewayDiscordClient client, String message) {

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        super.onSubscribe(channel, subscribedChannels);
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        super.onPUnsubscribe(pattern, subscribedChannels);
    }
}
