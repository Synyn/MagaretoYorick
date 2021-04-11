package com.magareto.yorick.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.db.redis.model.settings.GuildSettings;
import com.magareto.yorick.db.redis.model.settings.OsuTrackingSettings;
import com.magareto.yorick.service.SettingsService;
import discord4j.common.util.Snowflake;
import redis.clients.jedis.Jedis;

public class SettingsServiceImpl implements SettingsService {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Check if tracking is enabled in this guild
     * <p>
     * Returns true if tracking is enabled, returns false if tracking is disabled
     * <p>
     * If there are no settings, then creates a new default one.
     */
    @Override
    public Boolean checkIfTracked(Snowflake guildId) throws JsonProcessingException {
        String settingsJson = Globals.redisConnection.get(guildId.asString());

        if (settingsJson == null) {
            return null;
        }

        GuildSettings settings = mapper.readValue(settingsJson, GuildSettings.class);
        return settings.getOsuTrackingSettings().isTracking();

    }

    /**
     * Creates a new default GuildSettings object
     */
    @Override
    public GuildSettings create(Snowflake guildId) throws JsonProcessingException {

        GuildSettings settings = new GuildSettings();

        OsuTrackingSettings trackingSettings = new OsuTrackingSettings();
        trackingSettings.setTracking(false);
        settings.setOsuTrackingSettings(trackingSettings);

        persist(guildId, settings);

        return settings;
    }

    /**
     * Persists a GuildSettings object.
     */
    @Override
    public void persist(Snowflake guildId, GuildSettings guildSettings) throws JsonProcessingException {
        Globals.redisConnection.set(guildId.asString(), mapper.writeValueAsString(guildSettings));
    }

    /**
     * Gets the GuildSettings for a specified guild. If not found - returns null.
     */
    @Override
    public GuildSettings get(Snowflake guildId) throws JsonProcessingException {
        String settingsJson = Globals.redisConnection.get(guildId.asString());

        if (settingsJson == null) {
            return null;
        }

        return mapper.readValue(settingsJson, GuildSettings.class);

    }

}
