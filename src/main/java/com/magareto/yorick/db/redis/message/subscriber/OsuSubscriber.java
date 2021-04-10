package com.magareto.yorick.db.redis.message.subscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.bot.constants.RedisConstants;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.db.redis.model.Channel;
import com.magareto.yorick.db.redis.model.osu.TrackedUser;
import com.magareto.yorick.osu.model.ScoreModel;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Set;

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

        logger.info("Channel -> " + ch.name());

        switch (ch) {
            case OSU -> handleOsuScore(client, message);
        }
    }

    private void handleOsuScore(GatewayDiscordClient client, String message) {

        logger.info("Inside handle osu score " + message);

        try {
            ScoreModel scoreModel = mapper.readValue(message, ScoreModel.class);

            Set<String> userList = Globals.redisConnection.smembers(RedisConstants.OSU_TRACK_LIST);
            TrackedUser currentUser = null;

            for (String user : userList) {
                TrackedUser trackedUser = mapper.readValue(user, TrackedUser.class);
                if (trackedUser.getUserId().equals(scoreModel.getUserId())) {
                    currentUser = trackedUser;
                    break;
                }
            }

            String guildId = currentUser.getDiscordData().getGuildId();
            String channelId = currentUser.getDiscordData().getChannelId();
            discord4j.core.object.entity.channel.Channel channel = client.getChannelById(Snowflake.of(channelId)).block();

            MessageChannel messageChannel = (MessageChannel) channel;

            messageChannel.createMessage(message).subscribe();


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
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
