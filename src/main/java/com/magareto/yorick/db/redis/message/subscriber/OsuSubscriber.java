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
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import org.apache.log4j.Logger;
import reactor.core.publisher.Mono;
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
        if (message.equals("ping")) {
            return;
        }

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
                logger.info("scoreModelId -> " + scoreModel.getUserId());
                logger.info("trackedUserId -> " + trackedUser.getUserId());
                if (trackedUser.getUserId().equals(scoreModel.getUserId())) {
                    logger.info("I`m here");
                    currentUser = trackedUser;
                    break;
                }
            }

            String userLink = null;

            switch (scoreModel.getServer()) {
                case BANCHO -> userLink = "https://osu.ppy.sh/users/";
            }

            String guildId = currentUser.getDiscordData().getGuildId();
            String channelId = currentUser.getDiscordData().getChannelId();
            discord4j.core.object.entity.channel.Channel channel = client.getChannelById(Snowflake.of(channelId)).block();

            User discordUser = client.getUserById(Snowflake.of(currentUser.getDiscordData().getUserId())).block();

            MessageChannel messageChannel = (MessageChannel) channel;


            String finalUserLink = userLink;
            logger.info("Message channel -> " + messageChannel.getId().asString());
            logger.info("beatmap name -> " + scoreModel.getBeatMapName());

            messageChannel.createEmbed(embedCreateSpec -> embedCreateSpec
                    .setTitle(scoreModel.getBeatMapName())
                    .setUrl(scoreModel.getBeatMapUrl())
                    .addField("Difficulty ", scoreModel.getDifficulty(), true)
                    .addField("BPM ", scoreModel.getBpm(), true)
                    .addField("Stars ", scoreModel.getStarRating(), true)
                    .addField("PP ", scoreModel.getPp(), true)
                    .addField("Rank ", scoreModel.getRank(), true)
                    .addField("Score ", String.valueOf(scoreModel.getScore()), true)
                    .addField("Combo", String.valueOf(scoreModel.getMaxCombo()), true)
                    .addField("Miss count", scoreModel.getMissCount(), true)
                    .addField("Mode ", scoreModel.getGameMode(), true)

                    .setAuthor(scoreModel.getUsername(), finalUserLink + scoreModel.getUserId(), "https://osu.ppy.sh" + scoreModel.getUserAvatarUrl())

                    .setFooter(discordUser.getUsername() + " just landed a new score !", discordUser.getAvatarUrl())

            ).subscribe();


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
