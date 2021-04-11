package com.magareto.yorick.db.redis.message.subscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.bot.constants.RedisConstants;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.db.redis.model.Channel;
import com.magareto.yorick.db.redis.model.osu.TrackedUser;
import com.magareto.yorick.db.redis.model.settings.GuildData;
import com.magareto.yorick.db.redis.model.settings.GuildSettings;
import com.magareto.yorick.osu.model.ScoreModel;
import com.magareto.yorick.service.SettingsService;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import reactor.core.publisher.Mono;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Set;

public class OsuSubscriber extends JedisPubSub {
    private final GatewayDiscordClient client;

    ObjectMapper mapper = new ObjectMapper();

    Logger logger = Logger.getLogger(OsuSubscriber.class);

    SettingsService settingsService = Globals.injector.getInstance(SettingsService.class);


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
        /**
         * Check if tracking is enabled. If not enabled, then we do not do anything.
         */

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


            List<GuildData> guilds = currentUser.getDiscordData().getGuildList();

            GuildData contextGuild = null;

            for (GuildData guild : guilds) {
                GuildSettings settings = settingsService.get(Snowflake.of(guild.getGuildId()));
                if (settings == null) {
                    continue;
                }

                if (!settings.getOsuTrackingSettings().isTracking()) {
                    continue;
                }

                String trackingChannel = settings.getOsuTrackingSettings().getDefaultTrackingChannel();

                if (StringUtils.isEmpty(trackingChannel)) {
                    trackingChannel = guild.getChannelId();
                }

                sendScoreNotification(guild.getGuildId(), trackingChannel, currentUser.getDiscordData().getUserId(), scoreModel, client);

            }


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void sendScoreNotification(String guildId, String channelId, String discordUserId, ScoreModel scoreModel, GatewayDiscordClient client) {

        Guild guildById = client.getGuildById(Snowflake.of(guildId)).block();
        GuildChannel channel = guildById.getChannelById(Snowflake.of(channelId)).block();

        User discordUser = client.getUserById(Snowflake.of(discordUserId)).block();
        MessageChannel messageChannel = (MessageChannel) channel;

        logger.info("Message channel -> " + messageChannel.getId().asString());
        logger.info("beatmap name -> " + scoreModel.getBeatMapName());

        StringBuilder mods = new StringBuilder();

        if (!scoreModel.getMods().isEmpty()) {
            scoreModel.getMods().forEach(mods::append);
        }

        String userLink = null;

        switch (scoreModel.getServer()) {
            case BANCHO -> userLink = "https://osu.ppy.sh/users/";
        }

        String title = scoreModel.getBeatMapArtist() + " - " + scoreModel.getBeatMapName() + "[" + scoreModel.getDifficulty() + "]";

        String finalUserLink = userLink;
        messageChannel.createEmbed(embedCreateSpec -> embedCreateSpec
                .setTitle(title)
                .setUrl(scoreModel.getBeatMapUrl())
                .addField("Accuracy ", scoreModel.getAccuracy() + "%", false)
                .addField("Stars ", scoreModel.getStarRating(), true)
                .addField("BPM ", scoreModel.getBpm(), true)
                .addField("Rank ", scoreModel.getRank(), true)
                .addField("Mods ", scoreModel.getMods().isEmpty() ? "None" : mods.toString(), true)
                .addField("PP ", scoreModel.getPp(), true)
                .addField("Score ", String.valueOf(scoreModel.getScore()), true)
                .addField("Combo", String.valueOf(scoreModel.getMaxCombo()), true)
                .addField("Miss count", scoreModel.getMissCount(), true)
                .addField("Mode ", scoreModel.getGameMode(), true)

                .setAuthor(scoreModel.getUsername(), finalUserLink + scoreModel.getUserId(), scoreModel.getUserAvatarUrl().contains("https://a.ppy.sh") ? scoreModel.getUserAvatarUrl() : "https://osu.ppy.sh" + scoreModel.getUserAvatarUrl())

                .setFooter(discordUser.getUsername() + " just landed a new score !", discordUser.getAvatarUrl())

        ).subscribe();
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
