package com.magareto.yorick.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.bot.command.utils.CommandUtils;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.constants.RedisConstants;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.db.redis.model.osu.DiscordData;
import com.magareto.yorick.db.redis.model.osu.TrackedUser;
import com.magareto.yorick.db.redis.model.settings.GuildData;
import com.magareto.yorick.db.redis.model.settings.GuildSettings;
import com.magareto.yorick.osu.OsuServer;
import com.magareto.yorick.service.OsuService;
import com.magareto.yorick.service.SettingsService;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OsuServiceImpl implements OsuService {

    Logger logger = Logger.getLogger(OsuServiceImpl.class);

    ObjectMapper mapper = new ObjectMapper();

    private SettingsService settingsService = Globals.injector.getInstance(SettingsService.class);

    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    @Override
    public void trackNewUser(Message message) throws JsonProcessingException, YorickException {

        Optional<Snowflake> snowflakeGuildId = message.getGuildId();
        /**
         * Check if tracking is enabled for guild.
         *
         * If there are no settings yet for this guild, we create new default ones.
         * If the are settings for this guild we check if the guild has enabled tracking.
         *
         * If the guild did not enable tracking - we just skip this...
         *
         */
        Snowflake guildId = snowflakeGuildId.get();
        Boolean tracked = settingsService.checkIfTracked(guildId);

        if (tracked == null) {
            settingsService.create(guildId);
            return;
        } else if (!tracked) {
            return;
        }

        Matcher matcher = urlPattern.matcher(message.getContent());
        if (!matcher.find()) {
            return;
        }

        int start = matcher.start();
        int end = matcher.end();

        String link = message.getContent().substring(start, end);

        String[] split = StringUtils.split(link, "/");

        if (split.length < 3) {
            return;
        }

        OsuServer server = OsuServer.getServerByName(split[1]);

        if (server == null) {
            throw new YorickException("This is not a valid osu server or it is not yet implemented.");
        }

        String userId = split[3];

        Set<String> trackedUsers = Globals.redisConnection.smembers(RedisConstants.OSU_TRACK_LIST);

        TrackedUser currentContextUser = null;

        for (String userJson : trackedUsers) {
            TrackedUser trackedUser = mapper.readValue(userJson, TrackedUser.class);

            if (trackedUser.getServer() == server && trackedUser.getUserId().equals(userId)) {
                currentContextUser = trackedUser;
            }
        }

        User user = message.getAuthor().get();
        Snowflake channelId = message.getChannelId();

        if (currentContextUser == null) {
            createNewTrackedUser(user, userId, server, guildId, channelId);
        } else {
            addGuildIdToExistingUser(currentContextUser, guildId, channelId);
        }


        CommandUtils.sendMessage(message.getChannel(), "User is being tracked now..");

    }

    private void addGuildIdToExistingUser(TrackedUser currentContextUser, Snowflake guildId, Snowflake channelId) throws JsonProcessingException, YorickException {

        List<GuildData> guildList = currentContextUser.getDiscordData().getGuildList();
        if (guildList == null) {
            guildList = new ArrayList<>();
        } else {

            GuildData guildData = guildList.stream().filter(g -> g.getGuildId().equals(guildId.asString())).findFirst().orElse(null);
            if(guildData != null) {
                throw new YorickException(ErrorMessages.OSU_USER_IS_TRACKED);
            }
        }

        GuildData guildData = new GuildData();
        guildData.setGuildId(guildId.asString());
        guildData.setChannelId(channelId.asString());

        guildList.add(guildData);

        Globals.redisConnection.srem(RedisConstants.OSU_TRACK_LIST, mapper.writeValueAsString(currentContextUser));
        currentContextUser.getDiscordData().setGuildList(guildList);

        Globals.redisConnection.sadd(RedisConstants.OSU_TRACK_LIST, mapper.writeValueAsString(currentContextUser));

    }

    private void createNewTrackedUser(User user, String osuUserId, OsuServer server, Snowflake guildId, Snowflake channelId) throws JsonProcessingException {

        ArrayList<GuildData> guilds = new ArrayList<>();

        GuildData guildData = new GuildData();

        guildData.setChannelId(channelId.asString());
        guildData.setGuildId(guildId.asString());
        guilds.add(guildData);


        TrackedUser trackedUser = new TrackedUser();
        trackedUser.setUserId(osuUserId);
        trackedUser.setServer(server);
        trackedUser.setLastScoreDate(Calendar.getInstance());

        DiscordData discordData = new DiscordData();
        discordData.setUserId(user.getId().asString());

        discordData.setGuildList(guilds);

        trackedUser.setDiscordData(discordData);

        String data = mapper.writeValueAsString(trackedUser);
        logger.info("Data -> " + data);

        Globals.redisConnection.sadd(RedisConstants.OSU_TRACK_LIST, data);
    }


}
