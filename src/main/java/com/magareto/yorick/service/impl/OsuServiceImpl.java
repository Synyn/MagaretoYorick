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
import com.magareto.yorick.osu.OsuServer;
import com.magareto.yorick.service.OsuService;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OsuServiceImpl implements OsuService {

    Logger logger = Logger.getLogger(OsuServiceImpl.class);

    ObjectMapper mapper = new ObjectMapper();

    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    @Override
    public void trackNewUser(Message message) throws JsonProcessingException, YorickException {

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

        for (String userJson : trackedUsers) {
            TrackedUser trackedUser = mapper.readValue(userJson, TrackedUser.class);

            if (trackedUser.getServer() == server && trackedUser.getUserId().equals(userId)) {
                throw new YorickException(ErrorMessages.OSU_USER_IS_TRACKED);
            }
        }

        User user = message.getAuthor().get();

        TrackedUser trackedUser = new TrackedUser();
        trackedUser.setUserId(userId);
        trackedUser.setServer(server);
        trackedUser.setLastScoreDate(Calendar.getInstance());

        DiscordData discordData = new DiscordData();
        discordData.setUserId(user.getId().asString());
        discordData.setChannelId(message.getChannelId().asString());
        discordData.setGuildId(message.getGuildId().get().asString());

        trackedUser.setDiscordData(discordData);

        String data = mapper.writeValueAsString(trackedUser);
        logger.info("Data -> " + data);

        Globals.redisConnection.sadd(RedisConstants.OSU_TRACK_LIST, data);

        CommandUtils.sendMessage(message.getChannel(), "User is being tracked now..");

    }
}
